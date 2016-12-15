package main.java.dmod.prealert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Incident implements Comparable<Incident> {

	public String id = "";

	public String data = "";

	public Long createdTime;

	public String address = "";
	public String name = "";
	public String type = "";
	public String units = "";
	public String station = "";
	public String text = "";

	public boolean liveTwitterSent = false;
	public boolean fireTwitterSent = false;

	public boolean houseAlerted = false;
	public boolean MD812alerted = false;
	public boolean PA812alerted = false;
	public boolean A812alerted = false;
	public boolean A812Balerted = false;
	public boolean A812Calerted = false;
	public boolean A812Dalerted = false;
	public boolean E812alerted = false;
	public boolean E812Balerted = false;
	public boolean TK812alerted = false;
	public boolean F812alerted = false;
	public boolean HSC812alerted = false;
	public boolean U812alerted = false;

	static final Logger LOG = LoggerFactory.getLogger(Incident.class);

	public Incident(String idIn) {
		id = idIn;
		createdTime = System.currentTimeMillis();
	}

	public boolean isABox() {

		if (type.contains("APARTMENT FIRE") || type.contains("BUILDING FIRE") || type.contains("HOUSE FIRE")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean fireUnitsAlerted() {
		if (houseAlerted || E812alerted || E812Balerted || F812alerted || TK812alerted || HSC812alerted || U812alerted) {
			return true;
		} else {
			return false;
		}
	}

	public boolean battalionChiefDue() {
		if (data.contains("PFLBO") || data.contains("PF/BO") || data.contains("PFLB0") || data.contains("PF/B0")) {
			return true;
		} else {
			return false;
		}
	}

	public void printState() {
		LOG.info("ID: [" + id + "]");
		LOG.info("Units: [" + units + "]");
		LOG.info("Type: [" + type + "]");
		LOG.info("Address: [" + address + "]");
		LOG.info("Name: [" + name + "]");
		LOG.info("Station: [" + station + "]");
		LOG.info("Text: [" + text + "]");
		LOG.info("Live Twitter Sent: [" + liveTwitterSent + "]");
		LOG.info("Fire Twitter Sent: [" + fireTwitterSent + "]");
	}

	public boolean alertMD812() {
		if (MD812alerted == false && data.contains("MD812")) {
			MD812alerted = true;
			if (units.length() < 2) {
				units += ("MEDIC");
			} else {
				units += ("/MEDIC");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertPA812() {
		if (PA812alerted == false && data.contains("PA812")) {
			PA812alerted = true;
			if (units.length() < 2) {
				units += ("PARA.AMBO");
			} else {
				units += ("/PARA.AMBO");
			}
			return true;
		} else {
			return false;
		}
	}

	// A812 and XA812
	public boolean alertA812() {
		if (A812alerted == false && (data.contains("XA812,") || data.contains("XA812 ") || data.contains("A812,") || data.contains("A812 "))) {
			A812alerted = true;
			if (units.length() < 2) {
				units += "AMBO";
			} else {
				units += "/AMBO";
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertA812B() {
		if (A812Balerted == false && data.contains("A812B")) {
			A812Balerted = true;
			if (units.length() < 2) {
				units += ("B.AMBO");
			} else {
				units += ("/B.AMBO");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertA812C() {
		if (A812Calerted == false && data.contains("A812C")) {
			A812Calerted = true;
			if (units.length() < 2) {
				units += ("C.AMBO");
			} else {
				units += ("/C.AMBO");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertA812D() {
		if (A812Dalerted == false && data.contains("A812D")) {
			A812Dalerted = true;
			if (units.length() < 2) {
				units += ("D.AMBO");
			} else {
				units += ("/D.AMBO");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertE812() {
		if (E812alerted == false && (data.contains("E812 ") || data.contains("E812,"))) {
			E812alerted = true;
			if (units.length() < 2) {
				units += "ENGINE";
			} else {
				units += "/ENGINE";
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertE812B() {
		if (E812Balerted == false && data.contains("E812B")) {
			E812Balerted = true;
			if (units.length() < 2) {
				units += "PUMPER";
			} else {
				units += "/PUMPER";
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertTK812() {
		if (TK812alerted == false && data.contains("TK812")) {
			TK812alerted = true;
			if (units.length() < 2) {
				units += ("TRUCK");
			} else {
				units += ("/TRUCK");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertF812() {
		if (F812alerted == false && data.contains("F812")) {
			F812alerted = true;
			if (units.length() < 2) {
				units += "FOAM";
			} else {
				units += "/FOAM";
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertHSC812() {
		if (HSC812alerted == false && data.contains("HSC812")) {
			HSC812alerted = true;
			if (units.length() < 2) {
				units += ("HAZMAT");
			} else {
				units += ("/HAZMAT");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertU812() {
		if (U812alerted == false && data.contains("U812")) {
			U812alerted = true;
			if (units.length() < 2) {
				units += ("UTILITY");
			} else {
				units += ("/UTILITY");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean alertFirstDue() {
		if (houseAlerted == false && this.isABox() && station.equals("12")) {
			houseAlerted = true;
			units = "FIRST DUE";
			return true;
		}
		return false;
	}

	public int compareTo(Incident other) {
		// I could return the subtraction result
		// but might get overflow...
		if (this.createdTime == other.createdTime) {
			return 0;
		} else if ((this.createdTime - other.createdTime) > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	public void parseData(String incidentText) {
	}
}
