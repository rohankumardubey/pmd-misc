/*  
 * <copyright>  
 *  Copyright 1997-2003 PMD for Eclipse Development team
 *  under sponsorship of the Defense Advanced Research Projects  
 *  Agency (DARPA).  
 *   
 *  This program is free software; you can redistribute it and/or modify  
 *  it under the terms of the Cougaar Open Source License as published by  
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).   
 *   
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS   
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR   
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF   
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT   
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT   
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL   
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,   
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR   
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.   
 *   
 * </copyright>
 */
package net.sourceforge.pmd.eclipse;

import java.io.Writer;

import net.sourceforge.pmd.RuleSet;

/**
 * Interface for a rule set writer
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.4  2003/10/16 22:29:23  phherlin
 * Oops. Writing comments.
 *
 * Revision 1.3  2003/10/16 22:26:37  phherlin
 * Fix bug #810858.
 * Complete refactoring of rule set generation. Using a DOM tree and the Xerces 2 serializer.
 * 
 * -- Old version renamed to RuleSetWriterImpl --
 * -- Now RuleSetWriter is an interface        --
 *
 */
public interface RuleSetWriter {
    /**
     * Write a ruleset as an XML stream
     * @param writer the output writer
     * @param ruleSet the ruleset to serialize
     */
    void write(Writer writer, RuleSet ruleSet) throws PMDEclipseException;
}