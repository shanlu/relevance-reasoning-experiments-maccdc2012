# Evaluation of a Relevance Reasoning Method on MACCDC 2012 Dataset

This repository is an evaluation of relevance reasoning method on MACCDC 2012 dataset. The relevance reasoning method paper has been submitted to ISWC 2018 conference.


The U.S. National CyberWatch Mid-Atlantic Collegiate Cyber Defence Competition (MACCDC) provides college students with real life cyber defence experience. MACCDC official website: http://maccdc.org/

The MACCDC 2012 Dataset is the network traffic data collected during the competition in 2012 (http://www.netresec.com/?page=MACCDC). MACCDC 2012 is about a medical cyber security scenario. The event happened in a hospital whose computer systems are experiencing pretty intense cyber attacks. The hackers are trying to escalate the privileges on computer systems. The cyber security analysts need to take reactive actions in the available short time scales on the basis of their awareness of current attack situations.  http://www.secrepo.com/ provides Snort alsrts generated on the dataset. It has more than three million of alerts from a one and half day network traffic capture. We import the Snort alerts to the domain ontology as our knowledge base.

We select the Snort alerts captured from four five-minute time windows, and run twelve queries on these alerts.
