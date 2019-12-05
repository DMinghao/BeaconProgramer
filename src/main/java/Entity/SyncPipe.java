/*
 * The MIT License
 *
 * Copyright 2019 Shuyuan Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Entity;

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
