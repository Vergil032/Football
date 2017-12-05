package Util;

public class Timer {
    private long span;
    private long startTime=System.currentTimeMillis();
    
    public Timer(long span){
        this.span=span;
    }
    
    public boolean isTime(){
        return (System.currentTimeMillis()-startTime>span);
    }
    
    public void reset(){
        startTime=System.currentTimeMillis();
    }
}
