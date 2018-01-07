package main;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;  
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class JMSApp extends Application { 
	
	private MessageProducer messageProducer ;  
	private Session session ; 
	
	private TableView<Document> table ;  
	
	private String codeUser ; 	
	
	GsonBuilder builder = new GsonBuilder();
	Gson gson = builder.create(); 
	
    public static void main(String[] args) {
		Application.launch(JMSApp.class);

	} 

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("JMS App By Jawad Rizki"); 
		BorderPane borderPane = new BorderPane() ; 
		
		HBox header = new HBox();    
		header.setPadding(new Insets(10));  
		header.setSpacing(15); 
		
		header.setBackground(new Background(
				new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
				  		
		Label labelCode = new Label("Code :"); 
		TextField codeTextField  = new TextField("C1"); 
		codeTextField.setPromptText("Code");    
		
		Label labelHost = new Label("Host :"); 
		TextField hostTextField  = new TextField("localhost"); 
		hostTextField.setPromptText("Host");  
		
		Label labelPort = new Label(":"); 
		TextField portTextField  = new TextField("61616"); 
		portTextField.setPromptText("Port");    
		
		Button connectButton = new Button("Connexion");  
		
		
		header.getChildren().add(labelCode) ; 
		header.getChildren().add(codeTextField) ; 
		header.getChildren().add(labelHost) ; 
		header.getChildren().add(hostTextField) ;  
		header.getChildren().add(labelPort) ;  
		header.getChildren().add(portTextField) ;   
		header.getChildren().add(connectButton); 
		
		borderPane.setTop(header);  
		
		VBox vbx = new VBox() ;  
		
		vbx.setPadding(new Insets(10, 50, 50, 50));
		vbx.setSpacing(10);
		GridPane gridPane = new  GridPane() ; 
		
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		
		HBox hbox2 = new HBox() ; 
		vbx.getChildren().add(gridPane) ; 
		vbx.getChildren().add(hbox2); 
		borderPane.setCenter(vbx); 
		
		
		Label labaleTo = new Label("Destination :"); 
		TextField  consumer = new TextField("C1"); 
		consumer.setPrefWidth(250);   
		
		Label labelNom = new Label("Nom : ");   
		TextField  NomTextField = new TextField();  
		
		Label labelTaille = new Label("Taille : ");   
		TextField  tailleTextField = new TextField();
		
		Label labelFormat = new Label("Format : ");   
		TextField  FormatTextField = new TextField(); 
		
		// Creation des colomns de la TableView
		TableColumn<Document, String> nameDoc = new TableColumn<>("Nom");
		nameDoc.setMinWidth(200);  
		nameDoc.setCellValueFactory(new PropertyValueFactory<>("nom"));  
		
		TableColumn<Document, String> formatDoc = new TableColumn<>("Format");
		formatDoc.setMinWidth(200);  
		formatDoc.setCellValueFactory(new PropertyValueFactory<>("format"));
	
		TableColumn<Document, String> tailleDoc = new TableColumn<>("Taille");
		tailleDoc.setMinWidth(200);  
		tailleDoc.setCellValueFactory(new PropertyValueFactory<>("taille"));

		table = new TableView<>();  
		table.getColumns().addAll(nameDoc,formatDoc, tailleDoc) ; 
		
		hbox2.getChildren().addAll(table) ;   
		
		/*	Label labelMessage = new Label("Message : "); 
        TextArea	textareMessage = new TextArea();  
        textareMessage.setPrefWidth(250); */
         
        Button submit = new Button("Envoyer"); 
          
        
        gridPane.add(labaleTo, 0, 0);  
        gridPane.add(consumer, 1, 0);     
        
        gridPane.add(labelNom,0, 1);   
        gridPane.add(NomTextField,1,1 );  

        gridPane.add(labelFormat,0 ,2 );  
        gridPane.add(FormatTextField,1 , 2);  

        gridPane.add(labelTaille, 0,3 );  
        gridPane.add(tailleTextField, 1, 3);     
  
        
        gridPane.add(submit, 1, 4); 
       
        // Model       
        ObservableList<String>  ObservableListMessages =  
        		FXCollections.observableArrayList() ;    
        
        
        
        // Vue 
        
        Scene scene = new Scene(borderPane, 700, 600);    
        primaryStage.setScene(scene);
		primaryStage.show();   	
		
		// envoyer un document
		submit.setOnAction(e->{
			try {
				TextMessage textMessage =  session.createTextMessage();    
			
				String jsonDocument ="{"
						+ "  \"nom\"    : "+" \""+NomTextField.getText()+" \", "
						+ "  \"format\" : "+" \"" +FormatTextField.getText()+" \", "
						+ "  \"taille\" : "+Double.parseDouble(tailleTextField.getText())  
						+ "}";    
				
				textMessage.setText(jsonDocument);   
				textMessage.setStringProperty("code", consumer.getText()); 
				messageProducer.send(textMessage); 
				
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		});
		
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
  
			public void handle(ActionEvent event) {  
				try {
				 codeUser = codeTextField.getText() ;  
				String host = hostTextField.getText(); 
				int port = Integer.parseInt(portTextField.getText()) ; 
			   
				ConnectionFactory cf =
						new ActiveMQConnectionFactory("tcp://"+host+":"+port) ;   
				
					Connection conn = cf.createConnection() ;   
					conn.start();   
					 session  = conn.createSession(false, Session.AUTO_ACKNOWLEDGE) ; 
					 //session2 = conn.createSession(false, Session.AUTO_ACKNOWLEDGE) ; 
					 Destination dest = session.createTopic("jmsapp") ;  
					MessageConsumer messageConsumer = session.createConsumer(dest,"code='"+codeUser+"'") ;  
					
					messageProducer = session.createProducer(dest) ;  
					messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT) ;
					
					// l'affichage de mesage reçu 
					messageConsumer.setMessageListener(message->{
						try {
							if(message instanceof TextMessage){
								TextMessage textMessage = (TextMessage)message ;  
								// convert tm to object Document
								 
								Document document = gson.fromJson(textMessage.getText(), Document.class);
								ObservableList<Document> docs = table.getItems();  
								
								docs.add(document);        
							} 
							else if(message instanceof StreamMessage){
								
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				} catch (JMSException e) {
					e.printStackTrace();
				} 
				
				header.setDisable(true);  
				
			}
			
		});
		
	
				
	}
}