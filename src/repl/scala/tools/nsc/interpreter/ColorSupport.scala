/* NSC -- new Scala compiler
* Copyright 2005-2013 LAMP/EPFL
*/

package scala
package tools.nsc
package interpreter

import java.lang.Boolean.parseBoolean
import jline.TerminalFactory

object ColorSupport {

  lazy val colorEnabled = parseBoolean(Properties.propOrElse("scala.log.format", "true")) && ansiSupported

  private[this] def ansiSupported =
    try {
      val terminal = TerminalFactory.get
      terminal.restore
      terminal.isAnsiSupported
    } catch {
      case e: Exception => !Properties.isWin
    }
}
