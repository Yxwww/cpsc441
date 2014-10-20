package cpsc441.assign_1;


/**
 * Created by Yx on 10/10/2014.
 */
public class serverEngin {
    public static void main(String[] args){
        if(args.length>0) {
            System.out.println("command line input: "+args[0]);
            WebServer server = new WebServer(Integer.parseInt(args[0]));
            new Thread(server).start();
            try {
                Thread.sleep(120000 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Stopping Server");
            server.stop();
        }else{
            System.out.println("command empty");
        }
    }
}
