/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.objects;

/**
 *
 * @author kyle
 */
public interface UserDataInterface
{

    public static final String KEY_OBJECT_TYPE = "objType";

    public ObjectType getType();

    public String serialize();

    public String getFileName();
    
    public int getNumParams();
    
    public Object[] getAllParams();
}
