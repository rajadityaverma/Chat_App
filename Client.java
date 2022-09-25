import java.net.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;

import java.io.*;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;
 
    //Declare component
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);


    //constructor
    public Client  (){
       try{
          
           System.out.println("sending request to server");
           socket = new Socket("192.168.1.6", 5555);
           System.out.println("connection done");
 
           createGUI();
           handleEvents();
           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream());

           startReading();
           //startWriting();

       }catch(Exception e){
           e.printStackTrace();
       }
    }
       private void handleEvents(){
         messageInput.addKeyListener(new KeyListener(){
            @Override
             public void keyTyped(KeyEvent e){

             }
             @Override
             public void keyPressed(KeyEvent e){

             }
             @Override
             public void keyReleased(KeyEvent e){
                //System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10){
                  //  System.out.println("You have pressed ENTER button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
             }
         });
        
       }

       private void createGUI(){
        //GUI code
           this.setTitle("Client Messanger[END]");
           this.setSize(500,700);
           this.setLocationRelativeTo(null);
           this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
           //coding for component
           heading.setFont(font);
           messageArea.setFont(font);
           messageInput.setFont(font);

           heading.setIcon(new ImageIcon("new.png"));
           heading.setHorizontalTextPosition(SwingConstants.CENTER);
           heading.setVerticalTextPosition(SwingConstants.BOTTOM);

           heading.setHorizontalAlignment(SwingConstants.CENTER);
           heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

           messageArea.setEditable(false);
           messageInput.setHorizontalAlignment(SwingConstants.CENTER);
           //frame layout
           this.setLayout(new BorderLayout());
           
           //adding components to frame
          this.add(heading,BorderLayout.NORTH);
          JScrollPane jScrollPane= new JScrollPane(messageArea);
          this.add(jScrollPane,BorderLayout.CENTER);
          this.add(messageInput,BorderLayout.SOUTH);
          


          this.setVisible(true);
       }

    
    //start reading method
    public void startReading(){
        Runnable r1 = ()->{
           System.out.println("reader started..");
           try{
           while(true){
               
               String msg =br.readLine();
               if(msg.equals("exit")){
                 System.out.println("server terminate the chat");
                 JOptionPane.showMessageDialog(this, "Server terminated the chat");
                 messageInput.setEnabled(false);
                 break;
               }
               //System.out.println("server : "+msg);
                messageArea.append("Server : "+msg+"\n");

           }
          } catch(Exception e){
            System.out.println("connection is closed");
            //e.printStackTrace();
        }
         };
         new Thread(r1).start();
     }
     
     // start writing method
     public void startWriting(){
        Runnable r2 =()->{
            System.out.println("writer started...");
            try{
            while(!socket.isClosed()){
            
 
             BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
             String context = br1.readLine();
             out.println(context);
             out.flush();
             if(context.equals("exit")){
              socket.close();
              break;
          }
 
            
 
          }
          System.out.println("connection is closed");
        }catch(Exception e){
          e.printStackTrace();
      }
        };
         new Thread(r2).start();
     }
   
        public static void main(String[]args){
            System.out.println("this is server ... going to start");
            new Client();
        }
    
}
