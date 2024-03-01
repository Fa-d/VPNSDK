package de.blinkt.model;

public class SplitTunnelPackage {

    String packageTitle;
    String appName;
    Boolean isActive;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    public SplitTunnelPackage() {
    }

    public SplitTunnelPackage(String appName, String packageTitle, Boolean isActive) {
        this.appName = appName;
        this.packageTitle = packageTitle;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SplitTunnelPackage)) {
            return false;
        }
        SplitTunnelPackage otherMember = (SplitTunnelPackage)o;
        return otherMember.getPackageTitle().equals(getPackageTitle());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPackageTitle() == null) ? 0 : getPackageTitle().hashCode());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("'").append(packageTitle).append("'");
        sb.append('}');
        return sb.toString();
    }
}
