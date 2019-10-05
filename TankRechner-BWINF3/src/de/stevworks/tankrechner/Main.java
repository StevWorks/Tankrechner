package de.stevworks.tankrechner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Main {
	
	public static final String eingabeFile = "C:\\Users\\dijo\\Desktop\\eingabeFahrt2.txt";
	public static final String ausgabeFile = "C:\\Users\\dijo\\Desktop\\ausgabeFahrt.txt";
	
	public static double v; // Verbrauch (in Liter) - auf 100 Km
	public static int g; // Tankgröße (in Liter)
	public static double f; // verbleibende Tankfüllung (in Liter)
	public static int l; // Gesamtlänge (in km)
	public static int z; // Anzahl Tankstellen auf der Reiseroute
	
	public static int a = 0; // Anzahl angefahrener Tankstellen
	public static int gefahren = 0; // Gefahrene Strecke in km
	
	public static double r; // Restreichweite (in km)
	
	private static boolean aktiv = true;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		System.out.println("\n| Datei einlesen |\n");
		
		FileReader fr = new FileReader(eingabeFile);
		BufferedReader reader = new BufferedReader(fr);
		
		v = Integer.parseInt(reader.readLine());
		g = Integer.parseInt(reader.readLine());
		f = Double.parseDouble(reader.readLine());
		l = Integer.parseInt(reader.readLine());
		z = Integer.parseInt(reader.readLine());
		
		r = f / (v / 100); // Restreichweite
		
		String tankstellen [] [] = new String [z] [5];
		/*
		 * 0. Entfernung zur Tankstelle
		 * 1. Preis pro Liter
		 * 2. Bewertung (später)
		 * 3. Rang (später)
		 * 4. Angefahren? (true / false)
		 */
		
		System.out.println(v + " | " + g + " | " + f + " | " + l + " | " + z + "\n");
		
		for (int i = 0; i < z; i++) {
			String[] splitted = reader.readLine().split("    ");
			tankstellen[i][0] = splitted[0];
			tankstellen[i][1] = splitted[1];
			tankstellen[i][4] = String.valueOf(false);
		}
		
		for (String[] s : tankstellen) { // Debug - Tankstellen
			System.out.println(s[0] + " | " + s[1]);
		}
		
		int minTanken = (int) Math.ceil(l / (g / (v /100) + r)); // So oft muss getankt werden - aufgerundet //TODO: Nicht eingebunden
		System.out.println("\nMindestens anzufahren: " + minTanken);
		
		FileWriter fw = new FileWriter(ausgabeFile);
		BufferedWriter writer = new BufferedWriter(fw);
		
		if (l <= r) { // Es muss gar keine Tankstelle angefahren werden
			writer.write("Insgesamt angefahren: " + a);
			writer.close();
			System.exit(0);
		} else {
			while (aktiv == true) {
				int[] rating = new int [tankstellen.length];
				for (int i = 0; i < tankstellen.length; i++) {
					int mittelwert = (int) (((Double.parseDouble(tankstellen[i][0]) - Double.parseDouble(tankstellen[i][1]) * 25)) / 2) * 1000; // TODO
					tankstellen[i][2] = String.valueOf(mittelwert);
					rating[i] = mittelwert;
				}
				Arrays.sort(rating);
				for (int r = 0; r < rating.length; r++) {
					for (int i = 0; i < tankstellen.length; i++) {
						if (rating[r] == Integer.parseInt(tankstellen[i][2])) {
							tankstellen[i][3] = String.valueOf(r);
						} else {
						}
					}
				}
				for (String[] s : tankstellen) { // Debug - Tankstellen
					System.out.println(s[0] + " | " + s[1] + " | " + s[2] + " | " + s[3]);
				}
				
				System.out.println();
				
				for (int i = tankstellen.length - 1; i >= 0; i--) { // Ränge (Rating) durchgehen
					for (int j = 0; j < tankstellen.length; j++) { // Für alle Tankstellen durchgehen
						 if (Integer.parseInt(tankstellen[j][3]) == i) { // Stimmt der Rang? (Rating)
							if (Integer.parseInt(tankstellen[j][0]) - gefahren <= r && Boolean.parseBoolean(tankstellen[j][4]) != true) { // Liegt die Tankstelle auch in Reichweite?
								gefahren = Integer.parseInt(tankstellen[j][0]);
								f = f - ((v / 100) * (r - Integer.parseInt(tankstellen[j][0]))); // Update: Tankfüllung, wenn wir ankommen
								double lPreis = Double.parseDouble(tankstellen[j][1]); // Preis pro Liter an der Tankstelle
								double tanken;
								if (g  >= ((v / 100) * (l - gefahren))) { // Kommen wir mit weniger als Volltanken bis zum Ziel?
									tanken = (l - gefahren) * (v / 100); // Benötigte Liter bis zum Ziel
									aktiv = false;
								} else {
									tanken = g - f; // Liter, die noch in den Tank passen
								}
								double preis = Math.round((lPreis * tanken) * 100) / 100.0; // Preis bei Volltanken - Bei Bedacht des noch vorhandenen Tankinhalts
								f += tanken; // Neue Tankfüllung
								r = f / (v / 100); // Neue Restreichweite
								for (int k = 0; k < tankstellen.length; k++) {
									if (Integer.parseInt(tankstellen[k][0]) <= Integer.parseInt(tankstellen[j][0])) {
										tankstellen[k][4] = String.valueOf(true); // Tankstelle ist angefahren auf true setzen und alle davor
									}
								}
								writer.write(tankstellen[j][0] + " | " + tanken + " | " + preis);
								writer.newLine();
								a++;
								System.out.println(gefahren + " " + f + " " + lPreis + " " + tanken + " " + preis + " " + r + " " + a);
							}
						}
					}
				}
			}
		}
		
		
		writer.newLine();
		writer.write("Insgesamt angefahren: " + a);
		writer.close();
		
	}

}
