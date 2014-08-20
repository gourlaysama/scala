/* NSC -- new Scala compiler
 * Copyright 2005-2013 LAMP/EPFL
 * @author Stephane Micheloud
 */

package scala.man1

import _root_.scala.tools.nsc.doc.Settings

/**
 *  @author Gilles Dubochet
 *  @version 1.0
 */
object scaladoc extends Command with SettingPrinter {
  import _root_.scala.tools.docutil.ManPage._

  protected def cn = new Error().getStackTrace()(0).getClassName()

  val settings = new Settings(s => (), s => ())

  val additionalSettingDescriptions: Map[String, AbstractText] = Map(
  "-diagrams" -> ("Diagram generation requires the graphviz " & Mono("dot") & " diagram tool " &
                  "to be installed."),
  "-diagrams-dot-path" -> ("By default, " & Mono("dot") & "is assumed to be on the user " & Mono("PATH") & ".")
  )

  val scaladocSettings: Seq[Definition] = settings.scaladocSpecific.filterNot(_.isPrivate).toSeq.sortBy(_.name).map(settingToDefinition)

  val scalaLink = Link("Scala 2", "http://scala-lang.org/")

  val name = Section("NAME",

    MBold(command) & " " & NDash & " Documentation generator for the " &
    scalaLink & " language")

  val synopsis = Section("SYNOPSIS",

    CmdLine(" [ " & Argument("options") & " ] " & Argument("source files")))

  val parameters = Section("PARAMETERS",

    DefinitionList(
      Definition(
        Mono(Argument("options")),
        "Command line options. See " & Link(Bold("OPTIONS"), "#options") &
        " below."),
      Definition(
        Mono(Argument("source files")),
        "One or more source files to be compiled (such as " &
        Mono("MyClass.scala") & ").")))

  val description = Section("DESCRIPTION",

    "The " & MBold(command) & " tool reads class and object definitions, " &
    "written in the " & scalaLink & " programming language, and generates " &
    "their API as HTML files.",

    "By default, the generator puts each HTML file in the same directory as " &
    "its source file. You can specify a separate destination directory with " &
    CmdOption("d") & " (see " & Link(Bold("OPTIONS"), "#options") & ", below).",

    // tags are defined in class "scala.tools.nsc.doc.DocGenerator"
    "The recognised format of comments in source is described in the " & Link("online documentation",
    "https://wiki.scala-lang.org/display/SW/Scaladoc"))

  val options = Section("OPTIONS",

    Section("Standard Options",
      DefinitionList(
        Definition(
          CmdOption("d", Argument("directory")),
          "Specify where to generate documentation."),
        Definition(
          CmdOption("version"),
          "Print product version and exit."),
        Definition(
          CmdOption("help"),
          "Print a synopsis of available options."),
        Definition(
          Mono(Argument("compiler-option")),
          "Any " & MBold("scalac") & " option.  See " &
          Link(Bold("scalac") & "(1)", "scalac.html") & "."))),

    Section("Documentation Options", DefinitionList(
      scaladocSettings:_*)))

  val exitStatus = Section("EXIT STATUS",

    MBold(command) & " returns a zero exit status if it succeeds at processing " &
    "the specified input files. Non zero is returned in case of failure.")

  override val authors = Section("AUTHORS",

    "This version of Scaladoc was written by Gilles Dubochet with contributions by Pedro Furlanetto and Johannes Rudolph. " &
    "It is based on the original Scaladoc (Sean McDirmid, Geoffrey Washburn, Vincent Cremet and Stéphane Micheloud), " &
    "on vScaladoc (David Bernard), as well as on an unreleased version of Scaladoc 2 (Manohar Jonnalagedda).")

  val seeAlso = Section("SEE ALSO",

    Link(Bold("fsc") & "(1)", "fsc.html") & ", " &
    Link(Bold("scala") & "(1)", "scala.html") & ", " &
    Link(Bold("scalac") & "(1)", "scalac.html") & ", " &
    Link(Bold("scalap") & "(1)", "scalap.html"))

  def manpage = new Document {
    title = command
    author = "Gilles Dubochet"
    sections = List(
      name,
      synopsis,
      parameters,
      description,
      options,
      exitStatus,
      authors,
      seeAlso)
  }
}
