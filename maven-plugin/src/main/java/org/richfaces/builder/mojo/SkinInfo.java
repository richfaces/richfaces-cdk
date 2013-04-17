/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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
package org.richfaces.builder.mojo;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.cdk.model.legacy.Resource;

/**
 * @author Maksim Kaszynski
 *
 */
public class SkinInfo implements Cloneable {
    private List<Resource> baseClassResources = new ArrayList<Resource>();
    private List<Resource> extClassResources = new ArrayList<Resource>();
    private String baseSkin;
    private Resource extendedXcss;
    private Resource masterXcss;
    private String packageName;
    private Resource propertyFile;
    private String shortName;
    private boolean useExt;

    public boolean isUseExt() {
        return useExt;
    }

    public void setUseExt(boolean useExt) {
        this.useExt = useExt;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Resource> getBaseClassResources() {
        return baseClassResources;
    }

    public void setBaseClassResources(List<Resource> baseClassResources) {
        this.baseClassResources = baseClassResources;
    }

    public List<Resource> getExtClassResources() {
        return extClassResources;
    }

    public void setExtClassResources(List<Resource> extClassResources) {
        this.extClassResources = extClassResources;
    }

    public Resource getMasterXcss() {
        return masterXcss;
    }

    public void setMasterXcss(Resource masterXcss) {
        this.masterXcss = masterXcss;
    }

    public Resource getExtendedXcss() {
        return extendedXcss;
    }

    public void setExtendedXcss(Resource extendedXcss) {
        this.extendedXcss = extendedXcss;
    }

    public Resource getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(Resource propertyFile) {
        this.propertyFile = propertyFile;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getBaseSkin() {
        return baseSkin;
    }

    public void setBaseSkin(String baseSkin) {
        this.baseSkin = baseSkin;
    }
}
