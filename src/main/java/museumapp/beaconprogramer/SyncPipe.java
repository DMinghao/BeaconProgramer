/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumapp.beaconprogramer;

import java.io.InputStream;
import java.io.OutputStream;


/**
 *
 * @author mdu18
 */
public class SyncPipe implements Runnable{
    
    final int buffBY = 2048; 
    
    private final OutputStream ostrm_; 
    private final InputStream istrm_; 
    
    
    public SyncPipe(InputStream istrm, OutputStream ostrm){
        this.istrm_ = istrm; 
        this.ostrm_ = ostrm; 
    }

    @Override
    public void run() {
        try{
            final byte[] buffer = new byte[buffBY]; 
            for(int length = 0; (length = istrm_.read(buffer)) != -1;) ostrm_.write(buffer, 0, length);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
