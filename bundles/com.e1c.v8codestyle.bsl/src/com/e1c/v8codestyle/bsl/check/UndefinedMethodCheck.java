/**
 * Copyright (C) 2022, 1C
 */
package com.e1c.v8codestyle.bsl.check;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

import com._1c.g5.v8.dt.bsl.model.BslPackage;
import com._1c.g5.v8.dt.bsl.model.FeatureAccess;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com.e1c.g5.v8.dt.check.CheckComplexity;
import com.e1c.g5.v8.dt.check.ICheckParameters;
import com.e1c.g5.v8.dt.check.components.BasicCheck;
import com.e1c.g5.v8.dt.check.settings.IssueSeverity;
import com.e1c.g5.v8.dt.check.settings.IssueType;

/**
 * Check for undefined function or procedure in module
 *
 * @author Vadim Geraskin
 */
public class UndefinedMethodCheck
    extends BasicCheck
{
    private static final String CHECK_ID = "undefined-method"; //$NON-NLS-1$

    @Override
    public String getCheckId()
    {
        return CHECK_ID;
    }

    @Override
    protected void check(Object object, ResultAcceptor resultAcceptor, ICheckParameters parameters,
        IProgressMonitor progressMonitor)
    {
        if (progressMonitor.isCanceled())
        {
            return;
        }
        Invocation invocation = (Invocation)object;
        FeatureAccess fa = invocation.getMethodAccess();
        if (fa instanceof StaticFeatureAccess)
        {
            String name = fa.getName();
            String msg;
            if (((StaticFeatureAccess)fa).getImplicitVariable() == null && invocation != null)
            {
                if (BslUtil.isProcedureInvocation(invocation))
                {
                    msg = NLS.bind(Messages.ModuleUndefinedMethod_msg, name);
                    resultAcceptor.addIssue(msg, fa);
                }
            }
        }
    }

    @Override
    protected void configureCheck(CheckConfigurer configurationBuilder)
    {
        configurationBuilder.title(Messages.ModuleUndefinedMethodCheck_Title)
            .description(Messages.ModuleUndefinedMethodCheck_Description)
            .complexity(CheckComplexity.NORMAL)
            .severity(IssueSeverity.MAJOR)
            .issueType(IssueType.ERROR)
            .module()
            .checkedObjectType(BslPackage.Literals.INVOCATION);
    }
}
