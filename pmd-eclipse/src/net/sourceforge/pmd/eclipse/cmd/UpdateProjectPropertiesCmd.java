/*
 * Created on 21 nov. 2004
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

import net.sourceforge.pmd.RuleSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkingSet;

/**
 * Save updated project properties. This is a composite command.
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
public class UpdateProjectPropertiesCmd extends JobCommand {
    private IProject project;
    private boolean pmdEnabled;
    private IWorkingSet projectWorkingSet;
    private RuleSet projectRuleSet;
    private boolean ruleSetStoredInProject;
    private boolean needRebuild;
    private boolean ruleSetFileNotFound;
    
    /**
     * Default constructor. Initializes command attributes
     *
     */
    public UpdateProjectPropertiesCmd() {
        super("Updating project properties");
        setReadOnly(false);
        setOutputData(true);
        setName("UpdateProjectProperties");
        setDescription("Update a project PMD specific properties.");
    }

    /**
     * @see net.sourceforge.pmd.eclipse.cmd.DefaultCommand#execute()
     */
    protected IStatus execute() throws CommandException {
        if (this.project == null) throw new MandatoryInputParameterMissingException("project");
        if (this.projectRuleSet == null) throw new MandatoryInputParameterMissingException("projectRuleSet");

        this.getMonitor().beginTask("Updating project properties", 4);
        UpdatePmdEnabledPropertyCmd pmdEnabledCmd = new UpdatePmdEnabledPropertyCmd();
        if (!this.getMonitor().isCanceled()) {
            this.getMonitor().subTask("Updating PMD enabling state");
            pmdEnabledCmd.setProject(this.project);
            pmdEnabledCmd.setPmdEnabled(this.pmdEnabled);
            pmdEnabledCmd.execute();
            this.getMonitor().worked(1);
        }
        
        UpdateProjectRuleSetCmd projectRuleSetCmd = new UpdateProjectRuleSetCmd();
        if (!this.getMonitor().isCanceled()) {
            this.getMonitor().subTask("Updating project rule set");
            projectRuleSetCmd.setProject(this.project);
            projectRuleSetCmd.setProjectRuleSet(this.projectRuleSet);
            projectRuleSetCmd.execute();
            this.getMonitor().worked(1);
        }
        
        UpdateProjectWorkingSetCmd projectWorkingSetCmd = new UpdateProjectWorkingSetCmd();
        if (!this.getMonitor().isCanceled()) {
            this.getMonitor().subTask("Updating project working set");
            projectWorkingSetCmd.setProject(this.project);
            projectWorkingSetCmd.setProjectWorkingSet(this.projectWorkingSet);
            projectWorkingSetCmd.execute();
            this.getMonitor().worked(1);
        }
        
        UpdateRuleSetStoredInProjectPropertyCmd ruleSetStoredInProjectCmd = new UpdateRuleSetStoredInProjectPropertyCmd();
        if (!this.getMonitor().isCanceled()) {
            this.getMonitor().subTask("Updating rule set location state");
            ruleSetStoredInProjectCmd.setProject(this.project);
            ruleSetStoredInProjectCmd.setRuleSetStoredInProject(this.ruleSetStoredInProject);
            ruleSetStoredInProjectCmd.execute();
            this.ruleSetFileNotFound = ruleSetStoredInProjectCmd.isRuleSetFileNotFound();
            this.getMonitor().worked(1);
        }
        
        if (!this.getMonitor().isCanceled()) {
            this.needRebuild = ruleSetStoredInProjectCmd.isNeedRebuild();
            if (this.pmdEnabled) {
                if (!this.ruleSetStoredInProject) {
                    this.needRebuild |= projectRuleSetCmd.isNeedRebuild();
                }
                this.needRebuild |= pmdEnabledCmd.isNeedRebuild();
                this.needRebuild |= projectWorkingSetCmd.isNeedRebuild();
            }
        }

        this.getMonitor().done();
        return this.getMonitor().isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(IProject project) {
        this.project = project;
    }

    /**
     * @param pmdEnabled The pmdEnabled to set.
     */
    public void setPmdEnabled(boolean pmdEnabled) {
        this.pmdEnabled = pmdEnabled;
    }
    
    /**
     * @param projectRuleSet The projectRuleSet to set.
     */
    public void setProjectRuleSet(RuleSet projectRuleSet) {
        this.projectRuleSet = projectRuleSet;
    }
    
    /**
     * @param projectWorkingSet The projectWorkingSet to set.
     */
    public void setProjectWorkingSet(IWorkingSet projectWorkingSet) {
        this.projectWorkingSet = projectWorkingSet;
    }
    
    /**
     * @param ruleSetStoredInProject The ruleSetStoredInProject to set.
     */
    public void setRuleSetStoredInProject(boolean ruleSetStoredInProject) {
        this.ruleSetStoredInProject = ruleSetStoredInProject;
    }

    /**
     * @return Returns the needRebuild.
     */
    public boolean isNeedRebuild() {
        return needRebuild;
    }

    /**
     * @return Returns the ruleSetFileNotFound.
     */
    public boolean isRuleSetFileNotFound() {
        return ruleSetFileNotFound;
    }
}