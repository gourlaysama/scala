/* NSC -- new Scala compiler
 * Copyright 2005-2013 LAMP/EPFL
 * @author Stephane Micheloud
 */

package scala.man1

import _root_.scala.tools.nsc.settings.MutableSettings

/**
 *  @author Stephane Micheloud
 *  @version 1.0
 */
object scalac extends Command with SettingPrinter {
  import _root_.scala.tools.docutil.ManPage._

  protected def cn = new Error().getStackTrace()(0).getClassName()

  val settings = new MutableSettings(s => ())

  val additionalSettingDescriptions = Map(
  "-classpath" ->
    SeqPara("The format is platform-dependent: on Unix-based systems it should be " &
            "a colon-separated list of paths, and on Windows-based systems a " &
            "semicolon-separated list of paths. This does not override the " &
            "built-in (" & Mono("\"boot\"") & ") search path.",
            "The default class path is the current directory. Setting the " &
            Mono("CLASSPATH") & " variable or using the " & CmdOption("classpath") & " " &
            "command-line option overrides that default, so if you want to " &
            "include the current directory in the search path, you must " &
            "include " & Mono("\".\"") & " in the new settings."),
  "-encoding" ->
    SeqPara("The default value is platform-specific (Linux: " & Mono("\"UTF8\"") &
            ", Windows: " & Mono("\"Cp1252\"") & "). Executing the following " &
            "code in the Scala interpreter will return the default value " &
            "on your system:",
            MBold("    scala> ") &
            Mono("new java.io.InputStreamReader(System.in).getEncoding")),
  "-Xlint"   ->
    SeqPara("On top of the list above, additional unnamed lint warnings can appear if " &
    "at least one warning above is enabled.",
    CmdOption("Xlint") & " alone with no argument enables all lint warnings."
    )
  )

  val standardSettings: Seq[Definition] = settings.visibleSettings.filter(_.isStandard).toSeq.sortBy(_.name).map(settingToDefinition)

  val advancedSettings: Seq[Definition] = settings.visibleSettings.filter(_.isAdvanced).toSeq.sortBy(_.name).map(settingToDefinition)

  val name = Section("NAME",

    MBold(command) & " " & NDash & " Compiler for the " &
    Link("Scala 2", "http://scala-lang.org/") & " language")

  val synopsis = Section("SYNOPSIS",

    CmdLine(" [ " & Argument("options") & " ] " &
            Argument("source files")))

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
    "written in the Scala programming language, and compiles them into " &
    "bytecode class files.",

    "By default, the compiler puts each class file in the same directory " &
    "as its source file. You can specify a separate destination directory " &
    "with -d (see " & Link(Bold("OPTIONS"), "#options") & ", below).")

  val options = Section("OPTIONS",

    "The compiler has a set of standard options that are supported on the " &
    "current development environment and will be supported in future " &
    "releases. An additional set of non-standard options are specific to " &
    "the current virtual machine implementation and are subject to change " &
    "in the future.  Non-standard options begin with " & MBold("-X") & ".",

    Section("Standard Options", DefinitionList(standardSettings:_*)),

    Section("Advanced Options", DefinitionList(advancedSettings:_*)),

    Section("Compilation Phases",
      "Plugins and compiler options can disable some of the phases listed below, " &
      "as well as add additional phases. Run " & CmdOption("Xshow-phases") &
      " with a given set of options to show phases available in that particular " &
      "context.",
      DefinitionList(
        Definition(
          MItalic("parser"),
          "parse source into ASTs, perform simple desugaring"),
        Definition(
          MItalic("namer"),
          "resolve names, attach symbols to named trees"),
	Definition(
          MItalic("packageobjects"),
          "load package objects"),
	Definition(
          MItalic("typer"),
          "the meat and potatoes: type the trees"),
        Definition(
          MItalic("patmat"),
          "translate match expressions"),
	Definition(
          MItalic("superaccessors"),
          "add super accessors in traits and nested classes"),
	Definition(
          MItalic("extmethods"),
          "add extension methods for inline classes"),
	Definition(
          MItalic("pickler"),
          "serialize symbol tables"),
        Definition(
          MItalic("refchecks"),
          "reference/override checking, translate nested objects"),
	Definition(
          MItalic("selectiveanf"),
          "ANF pre-transform for " & MItalic("@cps") & " (if CPS plugin enabled)"),
	Definition(
          MItalic("selectivecps"),
          MItalic("@cps") & "-driven transform of selectiveanf assignements (if CPS plugin enabled)"),
	Definition(
          MItalic("uncurry"),
          "uncurry, translate function values to anonymous classes"),
        Definition(
          MItalic("tailcalls"),
          "replace tail calls by jumps"),
        Definition(
          MItalic("specialize"),
          MItalic("@specialized") & "-driven class and method specialization"),
        Definition(
          MItalic("explicitouter"),
          "this refs to outer pointers, translate patterns"),
        Definition(
          MItalic("erasure"),
          "erase types, add interfaces for traits"),
        Definition(
          MItalic("posterasure"),
          "clean up erased inline classes"),
        Definition(
          MItalic("lazyvals"),
          "allocate bitmaps, translate lazy vals into lazified defs"),
        Definition(
          MItalic("lambdalift"),
          "move nested functions to top level"),
        Definition(
          MItalic("constructors"),
          "move field definitions into constructors"),
        Definition(
          MItalic("flatten"),
          "eliminate inner classes"),
        Definition(
          MItalic("mixin"),
          "mixin composition"),
        Definition(
          MItalic("cleanup"),
          "platform-specific cleanups, generate reflective calls"),
        Definition(
          MItalic("delambdafy"),
          "remove lambdas"),
        Definition(
          MItalic("icode"),
          "generate portable intermediate code"),
        Definition(
          MItalic("inliner"),
          "optimization: do inlining"),
        Definition(
          MItalic("inlineHandlers"),
          "optimization: inline exception handlers"),
        Definition(
          MItalic("closelim"),
          "optimization: eliminate uncalled closures"),
        Definition(
          MItalic("constopt"),
          "optimization: optimize null and other constants"),
        Definition(
          MItalic("dce"),
          "optimization: eliminate dead code"),
        Definition(
          MItalic("jvm"),
          "generate JVM bytecode"),
        Definition(
          MItalic("terminal"),
          "the last phase in the compiler chain"),
        Definition(
          MItalic("all"),
          "matches all phases"))))

  val environment = Section("ENVIRONMENT",

    DefinitionList(
      Definition(
        MBold("JAVACMD"),
        "Specify the " & MBold("java") & " command to be used " &
        "for running the Scala code.  Arguments may be specified " &
        "as part of the environment variable; spaces, quotation marks, " &
        "etc., will be passed directly to the shell for expansion."),
      Definition(
        MBold("JAVA_HOME"),
        "Specify JDK/JRE home directory. This directory is used to locate " &
        "the " & MBold("java") & " command unless " & MBold("JAVACMD") & " variable set."),
      Definition(
        MBold("JAVA_OPTS"),
        SeqPara(
          "Specify the options to be passed to the " & MBold("java") &
          " command defined by " & MBold("JAVACMD") & ".",

          "With Java 1.5 (or newer) one may for example configure the " &
          "memory usage of the JVM as follows: " &
          Mono("JAVA_OPTS=\"-Xmx512M -Xms16M -Xss16M\""),

          "With " & Link("GNU Java", "http://gcc.gnu.org/java/") & " one " &
          "may configure the memory usage of the GIJ as follows: " &
          Mono("JAVA_OPTS=\"--mx512m --ms16m\"")
        ))))

  val examples = Section("EXAMPLES",

    DefinitionList(
      Definition(
        "Compile a Scala program to the current directory",
        CmdLine("HelloWorld")),
      Definition(
        "Compile a Scala program to the destination directory " &
        MBold("classes"),
        CmdLine(CmdOption("d", "classes") & "HelloWorld.scala")),
     Definition(
        "Compile a Scala program using a user-defined " & MBold("java") & " " &
        "command",
        MBold("env JAVACMD") & Mono("=/usr/local/bin/cacao ") &
        CmdLine(CmdOption("d", "classes") & "HelloWorld.scala")),
      Definition(
        "Compile all Scala files found in the source directory " &
        MBold("src") & " to the destination directory " &
        MBold("classes"),
        CmdLine(CmdOption("d", "classes") & "src/*.scala"))))

  val exitStatus = Section("EXIT STATUS",

    MBold(command) & " returns a zero exist status if it succeeds to " &
    "compile the specified input files. Non zero is returned in case " &
    "of failure.")

  val seeAlso = Section("SEE ALSO",

    Link(Bold("fsc") & "(1)", "fsc.html") & ", " &
    Link(Bold("scala") & "(1)", "scala.html") & ", " &
    Link(Bold("scaladoc") & "(1)", "scaladoc.html") & ", " &
    Link(Bold("scalap") & "(1)", "scalap.html"))

  def manpage = new Document {
    title = command
    date = "March 2012"
    author = "Stephane Micheloud"
    version = "1.0"
    sections = List(
      name,
      synopsis,
      parameters,
      description,
      options,
      environment,
      examples,
      exitStatus,
      authors,
      bugs,
      copyright,
      seeAlso)
  }
}
