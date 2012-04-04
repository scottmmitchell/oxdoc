/**

oxdoc (c) Copyright 2005 by Y. Zwols

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

package oxdoc.comments;

import oxdoc.*;

public class CommentSeeAlsoList extends BaseCommentBlock {
   public CommentSeeAlsoList(OxProject project) {
      super(project);
   }

   public boolean add(Object o) {
      String[] references = o.toString().split(",");
      for (int j = 0; j < references.length; j++)
         super.add(references[j].trim());

      return true;
   }

   protected String renderHTML() {
      String out = "";
      for (int j = 0; j < size(); j++) {
         if (j > 0)
            out += ", ";
         out += project.linkToSymbol(get(j).toString());
      }

      return out;
   }
}
