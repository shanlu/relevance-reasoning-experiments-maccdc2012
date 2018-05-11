package com.neu.shan.textparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;


public class Parser7 {

	static final String inputFileName1  = "/home/shanlu/Documents/ont/MACCDC/alert.fast.maccdc2012_00001.pcap";
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	
	static final String baseURI = "http://stix.mitre.org/STIX#";
	
	static final String cyboxFileName  = "/home/shanlu/Documents/ont/Cybox.owl";
	static final String cyboxURI = "http://cybox.mitre.org/cybox_v1";
	
	static final String capecFileName  = "/home/shanlu/Documents/ont/Capec.owl";
	static final String capecURI = "http://capec.mitre.org/capec_v1";
	
	static final String eventTrackerFilename = "/home/shanlu/Documents/ont/event_tracker.txt";
	
	public static void main(String[] args) throws IOException {
		
        File eventTrackerFile = new File(eventTrackerFilename);
        int scannedEventNumber = 0;
        if (eventTrackerFile.exists()) {
        	InputStream is = new FileInputStream(eventTrackerFile);
            scannedEventNumber = is.read();
            System.out.println("# lines skipped: " + scannedEventNumber);
            is.close();
        } else {
    		java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            java.lang.Process p = rt.exec("rm -fr /home/shanlu/Documents/ont/STIX.owl");
            java.lang.Process p2 = rt.exec("cp /home/shanlu/Documents/ont/temp/STIX_empty.owl /home/shanlu/Documents/ont/STIX.owl");
        }
        
		OntModel m = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + inputFileName + " not found");
	    }
	    m.read(in, null);
	    
//		OntModel mm = ModelFactory.createOntologyModel();
//	    InputStream inn = FileManager.get().open("/home/shanlu/Documents/ont/STIX_200.owl");
//	    if (inn == null) {
//	        throw new IllegalArgumentException("File: " + "/home/shanlu/Documents/ont/STIX_200.owl" + " not found");
//	    }
//	    mm.read(inn, null);
//	    
//		OntModel mmm = ModelFactory.createOntologyModel();
//	    InputStream innn = FileManager.get().open("/home/shanlu/Documents/ont/STIX_200_1.owl");
//	    if (innn == null) {
//	        throw new IllegalArgumentException("File: " + "/home/shanlu/Documents/ont/STIX_200_1.owl" + " not found");
//	    }
//	    mmm.read(innn, null);
//	    
//	    System.out.println(mm.equals(mmm));
	    
	    OntModel m1 = ModelFactory.createOntologyModel();
	    InputStream in1 = FileManager.get().open(cyboxFileName);
	    if (in1 == null) {
	        throw new IllegalArgumentException("File: " + cyboxFileName + " not found");
	    }
	    m1.read(in1, null);
	    
	    OntModel m2 = ModelFactory.createOntologyModel();
	    InputStream in2 = FileManager.get().open(capecFileName);
	    if (in2 == null) {
	        throw new IllegalArgumentException("File: " + capecFileName + " not found");
	    }
	    m2.read(in2, null);
	    
	    OntClass Observable = m1.getOntClass(cyboxURI + "#" + "Observable");
	    OntClass Indicator = m2.getOntClass(capecURI + "#" + "Indicator");
	    
	    OntProperty event = m.getOntProperty(baseURI + "event");
	    OntProperty observable = m.getOntProperty(baseURI + "observable");
	   
	    
	  //reading file line by line in Java using BufferedReader       
        FileInputStream fis = null;
        BufferedReader reader = null;
        
        LinkedList<String> temp = new LinkedList();
        
        try {
            fis = new FileInputStream(inputFileName1);
            reader = new BufferedReader(new InputStreamReader(fis));
          
            System.out.println("Reading File line by line using BufferedReader");
          
            String line = reader.readLine();
            
            int eventNumber = 0;
            for (eventNumber = 0; eventNumber < 29000; eventNumber++) {
            	line = reader.readLine();
            }
            
        	FileWriter out=null;
    	    try {
    	        out = new FileWriter( inputFileName );
    	    } catch (IOException e) {
    	        // TODO Auto-generated catch block
    	        e.printStackTrace();
    	    }
    	    
        	long start = System.currentTimeMillis();
        	System.out.println(start);
            
//            while(line != null){
            	for ( ; line != null; eventNumber++) {
//            while(eventNumber < 100){
            	
            	
            	if (eventNumber < scannedEventNumber) {
            		continue;
            	}

            	
            	int num = 0;
            	
            	for (String retval: line.split("\\[")){
                	temp.add(retval);
                	num++;
                 }
            	
            	if(num==6){
            	
        		String s1 = temp.get(0);
            	String dateTime = s1;
            	
            	String s2 = temp.get(2);
            	String sigId = s2.substring(0, s2.indexOf("]"));
            	String t1 = s2.substring(s2.lastIndexOf("]")+1);
            	String msg = t1.substring(1);
            	
            	String s3 = temp.get(4);
            	String t2 = s3.substring(0, s3.indexOf("]"));
            	String c = t2.substring(t2.lastIndexOf(":")+1);
            	String classification1 = c.substring(1);
            	String classification = classification1.substring(0, 1).toUpperCase() + classification1.substring(1);
            
            	String s4 = temp.get(5);
            	String t3 = s4.substring(s4.lastIndexOf("{")+1);
            	String protocol = t3.substring(0, t3.indexOf("}"));
            	String t5 = s4.substring(0, s4.indexOf("]"));
            	String t4 = t5.substring(t5.lastIndexOf(":")+1);
            	String priority = t4.substring(1);
            	
            	String s5 = s4.substring(s4.lastIndexOf("}")+1);
            	String t6 = s5.substring(1);
            	
            	String sIP;
            	String dIP;
            	
            	if (t6.indexOf(":")==-1){
            		sIP = t6.substring(0, t6.indexOf(" "));
            		dIP = t6.substring(t6.lastIndexOf(" ")+1);
            	}
            	else{
            	
            	sIP = t6.substring(0, t6.indexOf(":"));
            	String dPort = t6.substring(t6.lastIndexOf(":")+1);
            	String t7 = t6.substring(t6.lastIndexOf(" ")+1);
            	dIP= t7.substring(0, t7.indexOf(":"));
            	String t8 = t6.substring(0, t6.indexOf(" "));
            	String sPort = t8.substring(t8.lastIndexOf(":")+1);
            	
            	}
            
            	OntClass eventClass = m.getOntClass(baseURI + "SnortEvent");
            	OntClass cClass = m.getOntClass(baseURI + "Classification");
            	OntClass pClass = m.getOntClass(baseURI + "Protocol");
            	OntClass ipClass = m.getOntClass(baseURI + "IPAddress");
            	OntClass portClass = m.getOntClass(baseURI + "Port");
            	
            	
            	Individual i1 =  m.createIndividual(baseURI + "snort1Event" + eventNumber, eventClass);
            	
    	    	Individual obser = m.createIndividual(baseURI + "observable_" + "snort1Event" + eventNumber, Observable);
    	    	Individual id = m.createIndividual(baseURI + "indicator_" + "snort1Event" + eventNumber, Indicator);
    	    	
    	    	m.add(obser, event, i1);
    	    	m.add(id, observable, obser);
            	
            	
            	DatatypeProperty dt = m.getDatatypeProperty(baseURI + "dateTime");
                m.add(i1, dt, dateTime);
                
                DatatypeProperty sid = m.getDatatypeProperty(baseURI + "sig_id");
                m.add(i1, sid, sigId);
                
                DatatypeProperty mes = m.getDatatypeProperty(baseURI + "message");
                m.add(i1, mes, msg);
                
               
                ObjectProperty proto = m.getObjectProperty(baseURI + "protocol");
                
                ExtendedIterator insPro = pClass.listInstances();
                if (!insPro.hasNext()){
                	Individual i2 =  m.createIndividual(baseURI + protocol, pClass);
            		m.add(i1, proto, i2);
                }
                else{
                while(insPro.hasNext()){
                	
                	Individual indPro = (Individual) insPro.next();
                	if (indPro.getLocalName().equals(protocol) ){
                		Individual i2 =  m.getIndividual(baseURI + protocol);
                		m.add(i1, proto, i2);
                		break;
                	}
                }
                
                Individual i2 =  m.createIndividual(baseURI + protocol, pClass);
        		m.add(i1, proto, i2);
                }
                
                ObjectProperty clas = m.getObjectProperty(baseURI + "classification");
                DatatypeProperty text = m.getDatatypeProperty(baseURI + "text");
                DatatypeProperty pri = m.getDatatypeProperty(baseURI + "priority");
                
                ExtendedIterator instances = cClass.listInstances();
                
                while(instances.hasNext()){
                	
                	Individual ind = (Individual) instances.next();
                	
                	if(m.contains(ind, text, classification)){
                		m.add(i1, clas, ind);
                		m.add(ind, pri, priority);
                		break;
                	}
                	
                }
                
//                Individual i3 =  m.createIndividual(baseURI + "snortEvent" + eventNumber + "SourceIP", ipClass);
//                Individual i4 =  m.createIndividual(baseURI + "snortEvent" + eventNumber + "DestinationIP", ipClass);
                ObjectProperty sip = m.getObjectProperty(baseURI + "hasSourceIP");
                ObjectProperty dip = m.getObjectProperty(baseURI + "hasDestinationIP");
                DatatypeProperty ip = m.getDatatypeProperty(baseURI + "ip");
                String IPstring;
                String IPAddress;
                
                ExtendedIterator insIP = ipClass.listInstances();
                if (!insIP.hasNext()){
                	Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
                	IPstring = i3.toString();
                	IPAddress = IPstring.substring(IPstring.lastIndexOf("#")+1);
                	m.add(i3, ip, IPAddress);
                	Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
                	IPstring = i4.toString();
                	IPAddress = IPstring.substring(IPstring.lastIndexOf("#")+1);
                	m.add(i4, ip, IPAddress);
                	
                	m.add(i1, sip, i3);
                    m.add(i1, dip, i4);
                }
                
                else{
                while(insIP.hasNext()){
                	
                	Individual indIP = (Individual) insIP.next();
                	if (indIP.getLocalName().equals(sIP) ){
                		Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
                		m.add(i1, sip, i3);
                		break;
                	}
                	else if (indIP.getLocalName().equals(dIP) ){
                		Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
                		m.add(i1, dip, i4);
                		break;
                		
                	}
                }
                
                Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
            	Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
            	m.add(i1, sip, i3);
                m.add(i1, dip, i4);
                
                }
                
            	}
            	
            	else if(num==4){
            		
            		String s1 = temp.get(0);
        	    	String dateTime = s1;
        	    	
        	    	String s2 = temp.get(2);
        	    	String sigId = s2.substring(0, s2.indexOf("]"));
        	    	String t1 = s2.substring(s2.lastIndexOf("]")+1);
        	    	String msg = t1.substring(1);
        	    	
        	    	String s3 = temp.get(3);
        	    	String t3 = s3.substring(s3.lastIndexOf("{")+1);
        	    	String protocol = t3.substring(0, t3.indexOf("}"));
        	    	
        	    	String s5 = s3.substring(s3.lastIndexOf("}")+1);
        	    	String t6 = s5.substring(1);
        	    	
        	    	String sIP;
                	String dIP;
                	
                	if (t6.indexOf(":")==-1){
                		sIP = t6.substring(0, t6.indexOf(" "));
                		dIP = t6.substring(t6.lastIndexOf(" ")+1);
                	}
                	else{
        	    	
        	    	sIP = t6.substring(0, t6.indexOf(":"));
        	    	String dPort = t6.substring(t6.lastIndexOf(":")+1);
        	    	String t7 = t6.substring(t6.lastIndexOf(" ")+1);
        	    	dIP= t7.substring(0, t7.indexOf(":"));
        	    	String t8 = t6.substring(0, t6.indexOf(" "));
        	    	String sPort = t8.substring(t8.lastIndexOf(":")+1);
        	    	
                	}
        	    	
        	    	OntClass eventClass = m.getOntClass(baseURI + "SnortEvent");
                	OntClass pClass = m.getOntClass(baseURI + "Protocol");
                	OntClass ipClass = m.getOntClass(baseURI + "IPAddress");
                	OntClass portClass = m.getOntClass(baseURI + "Port");
                	
                	Individual i1 =  m.createIndividual(baseURI + "snort1Event" + eventNumber, eventClass);
                	
                	Individual obser = m.createIndividual(baseURI + "observable_" + "snort1Event" + eventNumber, Observable);
        	    	Individual id = m.createIndividual(baseURI + "indicator_" + "snort1Event" + eventNumber, Indicator);
        	    	
        	    	m.add(obser, event, i1);
        	    	m.add(id, observable, obser);
                	
                	DatatypeProperty dt = m.getDatatypeProperty(baseURI + "dateTime");
                    m.add(i1, dt, dateTime);
                    
                    DatatypeProperty sid = m.getDatatypeProperty(baseURI + "sig_id");
                    m.add(i1, sid, sigId);
                    
                    DatatypeProperty mes = m.getDatatypeProperty(baseURI + "message");
                    m.add(i1, mes, msg);
                    
                   
                    ObjectProperty proto = m.getObjectProperty(baseURI + "protocol");
                    ExtendedIterator insPro = pClass.listInstances();
                    if (!insPro.hasNext()){
                    	Individual i2 =  m.createIndividual(baseURI + protocol, pClass);
                		m.add(i1, proto, i2);
                    }
                    else{
                    while(insPro.hasNext()){
                    	
                    	Individual indPro = (Individual) insPro.next();
                    	if (indPro.getLocalName().equals(protocol) ){
                    		Individual i2 =  m.getIndividual(baseURI + protocol);
                    		m.add(i1, proto, i2);
                    		break;
                    	}
                    }
                    
                    Individual i2 =  m.createIndividual(baseURI + protocol, pClass);
            		m.add(i1, proto, i2);
                    }
                    
//                    Individual i3 =  m.createIndividual(baseURI + "snortEvent" + eventNumber + "SourceIP", ipClass);
//                    Individual i4 =  m.createIndividual(baseURI + "snortEvent" + eventNumber + "DestinationIP", ipClass);
                    ObjectProperty sip = m.getObjectProperty(baseURI + "hasSourceIP");
                    ObjectProperty dip = m.getObjectProperty(baseURI + "hasDestinationIP");
                    DatatypeProperty ip = m.getDatatypeProperty(baseURI + "ip");
                    String IPstring;
                    String IPAddress;
                    
                    ExtendedIterator insIP = ipClass.listInstances();
                    if (!insIP.hasNext()){
                    	Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
                    	IPstring = i3.toString();
                    	IPAddress = IPstring.substring(IPstring.lastIndexOf("#")+1);
                    	m.add(i3, ip, IPAddress);
                    	Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
                    	IPstring = i4.toString();
                    	IPAddress = IPstring.substring(IPstring.lastIndexOf("#")+1);
                    	m.add(i4, ip, IPAddress);
                    	
                    	m.add(i1, sip, i3);
                        m.add(i1, dip, i4);
                    }
                    
                    else{
                    while(insIP.hasNext()){
                    	
                    	Individual indIP = (Individual) insIP.next();
                    	if (indIP.getLocalName().equals(sIP) ){
                    		Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
                    		m.add(i1, sip, i3);
                    		break;
                    	}
                    	else if (indIP.getLocalName().equals(dIP) ){
                    		Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
                    		m.add(i1, dip, i4);
                    		break;
                    		
                    	}
                    }
                    
                    Individual i3 =  m.createIndividual(baseURI + sIP, ipClass);
                	Individual i4 =  m.createIndividual(baseURI + dIP, ipClass);
                	m.add(i1, sip, i3);
                    m.add(i1, dip, i4);
                    
                    }
                    
                    
            	}
            	
            	//cSystem.out.println(System.currentTimeMillis() - start);
            	

//            	out = new FileWriter( inputFileName );
//        	    try 
//        	    {
//        	        m.write( out, "RDF/XML" );
//        	    }
//        	    finally 
//        	    {
//        	       try 
//        	       {
//        	           
//        	       }
//        	       catch (IOException closeException) 
//        	       {
//        	           // ignore
//        	       }
//
//        	    }
//        	        out.close();
                

                line = reader.readLine();
                temp.clear();

        	    if (eventNumber > 0 && eventNumber % 100 == 0) {
        	    	System.out.println("Event " + eventNumber + " is finished!");
        	    	System.out.println(System.currentTimeMillis() - start);
        	    	System.out.println("m.size = " + m.size());
        	    }
        	    
        	    if (eventNumber > 0 && eventNumber % 1000 == 0) {
        	    	out = new FileWriter( inputFileName );
        	    	m.write( out, "RDF/XML" );
        	    	out.close();
        	    	OutputStream os = new FileOutputStream(eventTrackerFile);
        	    	os.write(eventNumber);
        	    	os.close();
//        	    	break;
        	    }
        	    
                if (eventNumber == 34000) {
                	break;
                }
                
                
            }
            
	    	out = new FileWriter( inputFileName );
	    	m.write( out, "RDF/XML" );
	    	out.close();

            
        } catch (FileNotFoundException ex) {
//          Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
//          Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE, null, ex);
        
      } finally {
          try {
              reader.close();
              fis.close();
          } catch (IOException ex) {
//              Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
		
	}
}


