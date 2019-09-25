/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

/**
 *
 * @author Home
 */
public class pro_class {
     public void addProduct(String Product_name, int Quantity, Object Price){
       String sql = "insert into product values(null, '"+Product_name+"', "+Quantity+", "+Price+")";
       System.out.println(sql);
    
}
}
