package com.dmod.prealert;

import jpcap.PacketReceiver;
import jpcap.packet.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

public class Reciever implements PacketReceiver {

	static final Logger LOG = LoggerFactory.getLogger(Driver.class);

	public Reciever() {
		LOG.info("Starting a receiver...");
	}

	public void receivePacket(Packet packetIn) {

		try {
			TCPPacket packet = (TCPPacket) packetIn;

			LOG.info("Transmission size: " + transmission.size() + " " + packet);
			if (packet.syn) {
				LOG.info("Got the SYN packet, here we go...");
				transmission = new ArrayList<TCPPacket>();
				transmission.add(packet);
			} else if (packet.fin) {
				LOG.info("...got the FIN packet");
				transmission.add(packet);

				StringBuilder builder = new StringBuilder();
				for (Packet p : transmission) {
					builder.append(new String(p.data, Charset.forName("US-ASCII")));
				}
				String rawTransmission = builder.toString();

				if (rawTransmission.contains("HP PCL6 Class Driver")) {
					// Probably a call
					LOG.info("Playing door alert...");
					try {
						LOG.info("ABOUT TO RASTER");
						startRastering(transmission);
						LOG.info("DONE RASTERING");
					} catch (Exception e) {
						LOG.error("ERROR trying to RASTER:", e);
					}
				} else if (rawTransmission.equals("") || rawTransmission.contains("Keep-Alive")) {
					// Keepalive
					LOG.info("--- keepalive ---");
				} else {
					// Something else?
					LOG.warn("!!!!! ----- UNABLE TO DETECT FOLLOWING MESSAGE TYPE ----- !!!!!");
					LOG.info("Raw Transmission: [" + rawTransmission + "]");
				}

			} else {
				transmission.add(packet);
			}

		} catch (Exception e) {
			LOG.error("Error while trying to parse a packet:", e);
		}
	}

	public static void startRastering(ArrayList<TCPPacket> transmission) throws Exception {
		String currentTime = String.valueOf(System.currentTimeMillis());

		writeToFile(transmission, currentTime);
		convertToTIFF(currentTime);
		doOCR(currentTime);
		String rawText = readFromFile(currentTime);
		LOG.info("Raw text: [" + rawText + "]");

		String prettyText = rawText.toUpperCase().trim();
		prettyText = prettyText.replaceAll("-", "");
		prettyText = prettyText.replaceAll("[^\\x00-\\x7F]", "");
		prettyText = prettyText.replaceAll(" +", " ");
		LOG.info("Pretty text: [" + prettyText + "]");

		if (prettyText.contains("ADDR")) {
			LOG.info("Transmission contained an incident, sending to Dispatcher...");
			Dispatcher.analyzeIncident(prettyText);
			LOG.info("Dispatcher done... dispatching...");
		} else {
			LOG.warn("Transmission DID NOT contain an incident");
		}

	}

	public static void writeToFile(ArrayList<TCPPacket> packets, String currentTime) throws Exception {
		FileOutputStream fop = null;
		File file;
		file = new File("working/" + currentTime + ".bits");
		fop = new FileOutputStream(file);

		if (!file.exists()) {
			file.createNewFile();
		}

		for (TCPPacket packet : packets) {
			fop.write(packet.data);
		}

		fop.flush();
		fop.close();

		LOG.info("Done writing bits: " + currentTime);
	}

	public static String readFromFile(String currentTime) throws Exception {

		Scanner input = new Scanner(new File("working/" + currentTime + ".txt"));
		StringBuilder builder = new StringBuilder();
		while (input.hasNextLine()) {
			String line = input.nextLine();
			// Newlines aern't included in nextLine(), add our own
			builder.append(line + " ");
		}

		input.close();
		return builder.toString();
	}

}