/*
 * Created on 20 nov. 2004
 *
 * Copyright (c) 2004, PMD for Eclipse Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * The end-user documentation included with the redistribution, if
 *       any, must include the following acknowledgement:
 *       "This product includes software developed in part by support from
 *        the Defense Advanced Research Project Agency (DARPA)"
 *     * Neither the name of "PMD for Eclipse Development Team" nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pmd.eclipse.cmd;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

/**
 * Query the working set associated with a project 
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2004/11/21 21:39:45  phherlin
 * Applying Command and CommandProcessor patterns
 *
 *
 */
public class QueryProjectWorkingSetCmd extends DefaultCommand {
    private IProject project;
    private IWorkingSet projectWorkingSet;
    
    /**
     * Default constructor. Initializes command attributes
     *
     */
    public QueryProjectWorkingSetCmd() {
        setReadOnly(true);
        setOutputData(true);
        setName("QueryProjectWorkingSet");
        setDescription("Query the working set associated with a project.");
    }

    /**
     * @see net.sourceforge.pmd.eclipse.cmd.DefaultCommand#execute()
     */
    protected void execute() throws CommandException {
        if (project == null) throw new MandatoryInputParameterMissingException("project");

        IWorkingSet workingSet = null;

        try {
            workingSet = (IWorkingSet) project.getSessionProperty(SESSION_PROPERTY_WORKINGSET);
            if (workingSet == null) {
                String workingSetName = project.getPersistentProperty(PERSISTENT_PROPERTY_WORKINGSET);
                if (workingSetName != null) {
                    IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
                    workingSet = workingSetManager.getWorkingSet(workingSetName);
                    if (workingSet != null) {
                        project.setSessionProperty(SESSION_PROPERTY_WORKINGSET, workingSet);
                    }
                }
            }
        } catch (CoreException e) {
            throw new CommandException("Exception when retreiving a project working set", e);
        }

        this.projectWorkingSet = workingSet;
    }

    /**
     * @return Returns the projectWorkingSet.
     */
    public IWorkingSet getProjectWorkingSet() {
        return projectWorkingSet;
    }
    
    /**
     * @param project The project to set.
     */
    public void setProject(IProject project) {
        this.project = project;
    }
}