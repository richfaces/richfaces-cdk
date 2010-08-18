/**
 * License Agreement.
 *
 * YUI Compressor Maven Mojo
 *
 * Copyright (C) 2007 Alchim31 Team
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package net.sf.alchim.mojo.yuicompressor;

/**
 * Check JS files with jslint.
 *
 * @goal jslint
 * @phase process-resources
 *
 * @author David Bernard
 * @created 2007-08-29
 */

//@SuppressWarnings("unchecked")
public class JSLintMojo extends MojoSupport {
    private JSLintChecker jslint_;

    @Override
    protected String[] getDefaultIncludes() throws Exception {
        return new String[] {"**/**.js"};
    }

    @Override
    public void beforeProcess() throws Exception {
        jslint_ = new JSLintChecker();
    }

    @Override
    public void afterProcess() throws Exception {}

    @Override
    protected void processFile(SourceFile src) throws Exception {
        getLog().info("check file :" + src.toFile());
        jslint_.check(src.toFile(), jsErrorReporter_);
    }
}
