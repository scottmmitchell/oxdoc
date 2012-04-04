/**

oxdoc (c) Copyright 2005-2009 by Y. Zwols

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/

package oxdoc;

import java.io.File;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class MathProcessorPlain extends MathProcessor {
   public OxDoc oxdoc = null;

   public MathProcessorPlain(OxDoc oxdoc) {
      super(oxdoc);
   }

   public String ProcessFormula(String formula, boolean isInline) {
      return formula;
   }

   public String ExtraHeader() {
      return "<style>.expression { font-style: italic; font-family: times; } .equation { font-style: italic; font-family: times; }</style>";
   }
}



