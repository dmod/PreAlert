package dmod.prealert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreAlert {


	private static final HashMap<String, Incident> calls = new HashMap<String, Incident>();

	public static final Timer timer = new Timer();

	static final Logger LOG = LoggerFactory.getLogger(Dispatcher.class);


	public static void analyzeIncident(String incidentText) {

		try {

			int colonIndex = incidentText.indexOf("ADDR:");
			int iIndex = incidentText.indexOf("ADDRI");
			int idIndex = colonIndex > iIndex ? colonIndex : iIndex;
			int nameIndex = incidentText.indexOf("NAME", idIndex);
			String id = incidentText.substring(idIndex + 6, nameIndex - 1);

			Incident incident;
			if (calls.containsKey(id)) {
				LOG.info("The ID: [" + id + "] was already in the calls, adding a new part");
				incident = calls.get(id);
			} else {
				LOG.info("----------- The ID: [" + id + "] is new, making a new printout -----------");
				incident = new Incident(id);
				calls.put(id, incident);
			}
			incident.parseData(incidentText);
			LOG.info("After parsing here's where we're at:");
			incident.printState();

			LOG.info("Welcome to alertUnits we hope you enjoy your stay");

			String color = null;

			if (incident.alertE812()) {
				LOG.info("E812 ALERTED!");
				color = "red";
			}
			if (incident.alertE812B()) {
				LOG.info("E812B ALERTED!");
				color = "red";
			}
			if (incident.alertF812()) {
				LOG.info("F812 ALERTED!");
				color = "green";
			}
			if (incident.alertMD812()) {
				LOG.info("MD812 ALERTED!");
				color = "orange";
			}
			if (incident.alertA812()) {
				LOG.info("A812 ALERTED!");
				color = "red";
			}
			if (incident.alertA812B()) {
				LOG.info("A812B ALERTED!");
				color = "red";
			}
			if (incident.alertA812C()) {
				LOG.info("A812C ALERTED!");
				color = "red";
			}
			if (incident.alertA812D()) {
				LOG.info("A812D ALERTED!");
				color = "red";
			}
			if (incident.alertTK812()) {
				LOG.info("TK812 ALERTED!");
				color = "green";
			}
			if (incident.alertHSC812()) {
				LOG.info("HSC812 ALERTED!");
				color = "green";
			}
			if (incident.alertU812()) {
				LOG.info("U812 ALERTED!");
				color = "colormix";
			}
			if (incident.alertFirstDue()) {
				// Down here so that the color takes precedence (rainbow yo)
				LOG.info("HOUSE ALERTED!");
				color = "rainbow1";
			}

			if (color != null) {
				LOG.info("Color was not null, in fact it was " + color);
				if (incident.battalionChiefDue()) {
					LOG.info(incident.id + " ... battalion chief due.., playing the door alert");
				}

				LOG.info("Number of calls: " + calls.size());
				while (calls.size() > 5) {
					ArrayList<Incident> sorted = new ArrayList<Incident>(calls.values());
					Collections.sort(sorted);

					Incident oldest = sorted.get(0);

					LOG.info("REMOVING: " + oldest.id + " " + oldest.units);
					calls.remove(oldest.id);
				}
			}
			incident.printState();
		} catch (Exception e) {
			LOG.error("Error while trying to analize incident: ", e);
		}
	}
	
}
