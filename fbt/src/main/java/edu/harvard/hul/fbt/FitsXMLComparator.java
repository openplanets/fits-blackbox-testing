package edu.harvard.hul.fbt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

public class FitsXMLComparator {

  public FitsXMLComparator() {
    XMLUnit.setCompareUnmatched(false);
    XMLUnit.setIgnoreAttributeOrder(true);
    XMLUnit.setIgnoreComments(true);
    XMLUnit.setIgnoreWhitespace(true);
    XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);

  }

  public int compare(String source, String candidate) {
    DetailedDiff myDiff;
    try {
      myDiff = new DetailedDiff(new Diff(source, candidate));
      myDiff.overrideElementQualifier(new ElementNameAndAttributeQualifier("toolname"));

      // if ( myDiff.similar() ) {
      // System.out.println( "XML similar: " );
      // }
      //
      // if ( myDiff.identical() ) {
      // System.out.println( "XML identical: " );
      // }

      List<Difference> allDifferences = myDiff.getAllDifferences();

      // TODO create MissingToolRule and invoke it here.
      // pass the differences and look for a
      // "Expected presence of child node 'xyz' but was 'null'"
      // this indicates that some node was missing. Inspect the missing node
      // closely and deduce whether it is a missing tool or not.

      // TODO figure out a way to pass the results back to the controller?
      // Callback vs Return Type?

      MissingToolOutputRule rule = new MissingToolOutputRule();
      rule.checkDifferences(allDifferences);
      
      if (rule.hasMissing()) {
        //System.out.println(Arrays.deepToString(rule.getMissingTools().toArray()));
        return ControllerState.TOOL_MISSING_OUTPUT;
      }

    } catch (SAXException e) {
      e.printStackTrace();
      return ControllerState.SYSTEM_ERROR;
    } catch (IOException e) {
      e.printStackTrace();
      return ControllerState.SYSTEM_ERROR;
    }

    return  ControllerState.OK;
  }
}
