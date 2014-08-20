/* NSC -- new Scala compiler
 * Copyright 2005-2013 LAMP/EPFL
 * @author  Martin Odersky
 */

package scala.man1

import _root_.scala.tools.docutil.ManPage._
import _root_.scala.tools.nsc.settings.MutableSettings

/**
*  Turns compiler Settings into scala.man1 Definitions.
*/
trait SettingPrinter { this: Command =>

  // commands implement this to provide additional text for specific settings
  val additionalSettingDescriptions: Map[String, AbstractText]

  def settingToDefinition(s: MutableSettings#Setting): Definition = {
    def trimName(n: String) = if (n.startsWith("-")) n.substring(1) else n

    def choices(c: List[String]): String = {
      val ch = c.mkString("{", ",", "}")
      if (ch.length < 80) ch else "{" + c.head + ",...}"
    }

    def withAbbreviations(print: String => AbstractText): AbstractText = {
      if (s.abbreviations.isEmpty) print(trimName(s.name))
      else print(trimName(s.name)) & SeqText(s.abbreviations.map(a => ", " & print(trimName(a))):_*)
    }

    def optionList(choices: List[String], descriptions: List[String], default: String = ""): AbstractText = {
      val length = choices.map(_.length).max + 4

      EmbeddedParagraph(BulletList(choices.zip(descriptions).map { a =>
        Mono(a._1.padTo(length, ' ')) & a._2 & (if (a._1 == default) " (default)" else "")
      }:_*))
    }

    def multiChoiceDescr(choices: List[String], descriptions: List[String], name: String, default: Option[List[String]]): AbstractText = {
       val list = optionList(choices, descriptions)

       val defaultDescr = default.fold("will throw an error.": AbstractText){ l =>
         "is equivalent to using " & CmdOptionBound(name, l.mkString(",") & ".")
       }

       SeqPara(list, CmdOptionBound(name, "_") & " to enable all, " & CmdOptionBound(name, "help") & " to list available options. " &
        "Individual options can be disabled by prefixing them with " & Mono("'-'") & ".", "Using " & CmdOption(name) & " alone with no argument " & defaultDescr)
    }

    val cmd = withAbbreviations { n => s match {
      case i: MutableSettings#IntSetting => CmdOption(n, Argument("integer"))
      case p: MutableSettings#PathSetting => CmdOption(n, Argument("path"))
      case s: MutableSettings#StringSetting => CmdOption(n, Argument(s.arg))
      case sc: MutableSettings#ScalaVersionSetting => CmdOptionBound(n, Argument("scala_version"))
      case m: MutableSettings#MultiChoiceSetting[_] => CmdOptionBound(n, choices(m.choices))
      case m: MutableSettings#ChoiceSetting => CmdOptionBound(n, choices(m.choices))
      case m: MutableSettings#MultiStringSetting => CmdOptionBound(n, Argument(m.arg))
      case p: MutableSettings#PhasesSetting => CmdOptionBound(n, "{" & Argument("phase1") & "," & Argument("phase2") & ",...}")
      case p: MutableSettings#PrefixSetting => Mono(Bold(p.prefix) & Italic(p.name.substring(p.prefix.length)))
      case s if s.name startsWith("@") => Mono(Bold("@") & Italic(s.name.substring(1)))
      case _ => CmdOption(n)
    }}

    val descr: AbstractText = s match {
      case m: MutableSettings#ChoiceSetting => SeqPara(m.shortDescription, optionList(m.choices, m.choiceDescriptions, m.default))
      case i: MutableSettings#IntSetting => SeqPara(i.helpDescription, s"default: ${i.default}" + i.range.map(r => s", range: ${r._1}-${r._2}").getOrElse(""))
      case m: MutableSettings#MultiChoiceSetting[_] => SeqPara(m.shortDescription, multiChoiceDescr(m.choices, m.descriptions, trimName(m.name), m.default))
      case m: MutableSettings#MultiStringSetting => SeqPara(m.helpDescription, "This option can be specified multiple times.")
      case p: MutableSettings#PhasesSetting => SeqPara(p.helpDescription, "Run " & CmdOption("Xshow-phases") & " to list available phases.")
      case _ => s.helpDescription
    }

    val fullDescr = additionalSettingDescriptions.get(s.name).fold(descr)(SeqPara(descr, _))

    Definition(cmd, fullDescr)
  }
}
