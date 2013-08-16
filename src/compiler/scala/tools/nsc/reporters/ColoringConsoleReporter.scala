/* NSC -- new Scala compiler
* Copyright 2002-2013 LAMP/EPFL
*/

package scala
package tools.nsc
package reporters

import java.io.{ BufferedReader, IOException, PrintWriter }
import scala.Console.{RED, RESET, YELLOW}
import scala.reflect.internal.util._


/**
* This class implements a Reporter that colors warning and error messages.
*/
class ColoringConsoleReporter(override val settings: Settings, reader: BufferedReader, writer: PrintWriter, val coloringEnabled: Boolean) extends ConsoleReporter(settings, reader, writer) {
  def this(settings: Settings) = this(settings, Console.in, new PrintWriter(Console.err, true), true)

  override def print(pos: Position, msg: String, severity: Severity) {
    if (coloringEnabled) severity match {
      case ERROR => printMessage(pos, RED + clabel(severity) + msg + RESET)
      case WARNING => printMessage(pos, YELLOW + clabel(severity) + msg + RESET)
      case INFO => printMessage(pos, clabel(severity) + msg)
    } else super.print(pos, msg, severity)
  }
}
