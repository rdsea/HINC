/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.ext.calvin.nodeproperties;

/**
 *
 * @author hungld
 */
public class Owner {

    String organization;
    String organizationalUnit;
    String role;
    String personOrGroup;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPersonOrGroup() {
        return personOrGroup;
    }

    public void setPersonOrGroup(String personOrGroup) {
        this.personOrGroup = personOrGroup;
    }

}
