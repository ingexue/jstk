package de.fau.cs.jstk.sampled;

import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MixerUtil {

	/**
	 * Find a mixer that matches a given name.
	 * 
	 * @param mixerName as returned by a previous call of Mixer.Info.getName()
	 * @param forRecording whether looking for a recording device (or playback otherwise). This 
	 * information is needed because otherwise, mixer names are ambiguous. 
	 * @return the matching Mixer.Info from AudioSystem.getMixerInfo() or null when there's no match
	 * @throws Exception for multiple matches
	 */
	public static Mixer.Info getMixerInfoFromName(String mixerName, boolean forRecording) throws Exception{
		Mixer.Info [] availableMixers = AudioSystem.getMixerInfo();
		
		Mixer.Info info = null;
		
		for (Mixer.Info m : availableMixers){
			// don't consider playing mixers - otherwise, the mixer names are ambiguous
			if (!AudioSystem.getMixer(m).isLineSupported(
					forRecording ? 
							new Info(TargetDataLine.class) :
								new Info(SourceDataLine.class))){
				//System.out.println(mixerName + ": Not considering");
			
				continue;
			}
			if (m.getName().trim().equals(mixerName)){
				if (info != null){
					throw new Exception(String.format("multiple matches for \"%s\": \"%s\" and \"%s\"",
							mixerName, info, m.getName()));
				}
				info = m;
			}
		}
		return info;
	}
	
	
	
	/**
	 * Returns all mixers that support a given playback/recording mode, and optionally support a given
	 * AudioFormat.
	 * 
	 * @param af if not null, try to return only those mixers that work with af. 
	 * @param forRecording whether looking for a recording device (or playback otherwise).
	 * @return array of matching mixers
	 */
	public static Mixer.Info [] getMixerList(AudioFormat af, boolean forRecording) {
		if (af != null){
			System.err.println("AudioCapture.getMixerList: retrieving only mixers that work with format " + af + 
					(forRecording ? " for recording" : " for playback"));

			Mixer.Info [] list = AudioSystem.getMixerInfo();
			List<Mixer.Info> working = new LinkedList<Mixer.Info>();
			
			DataLine.Info lineInfo = new DataLine.Info(forRecording ? TargetDataLine.class : SourceDataLine.class,
					af);
			
			for (Mixer.Info i : list) {
				
				Mixer mixer = AudioSystem.getMixer(i);
				System.out.println(mixer.getMixerInfo().toString() + ": checking... with + " + af.toString());
				if (!mixer.isLineSupported(
						forRecording ? 
								new Info(TargetDataLine.class) :
									new Info(SourceDataLine.class))){
					System.out.println(mixer.getMixerInfo().toString() + ": Not considering");
				
					continue;
				}
				
				DataLine dataline;
				dataline = null;
				try {
					
					
					dataline = (DataLine) mixer.getLine(lineInfo);
					
//					if (forRecording)
//						((TargetDataLine)dataline).open(af);
//					else
//						((SourceDataLine)dataline).open(af);
					
					//dataline.start();
					
					System.out.println(mixer.getMixerInfo().toString() + ": OK");
					working.add(i);
				} catch (Exception e) {		
					System.out.println(mixer.getMixerInfo().toString() + ": Not ok");
					e.printStackTrace();
				}				
				finally{
					if (dataline != null) {
						try {
							dataline.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}	
			Mixer.Info [] dummy = new Mixer.Info[0];
			return working.toArray(dummy);
		}
		else{
			Mixer.Info [] list = AudioSystem.getMixerInfo();			
			
			List<Mixer.Info> rightTypes = new LinkedList<Mixer.Info>();
			
			for (Mixer.Info i : list) {
				Mixer mixer = AudioSystem.getMixer(i);
				if (mixer.isLineSupported(
						forRecording ? 
								new Info(TargetDataLine.class) :
									new Info(SourceDataLine.class))){
					System.out.println(mixer.getMixerInfo().toString() + " is suited for " + 
							(forRecording ? "recording" : "playback"));
					rightTypes.add(i);
				}
				else{
					System.out.println(mixer.getMixerInfo().toString() + " is NOT suited for " + 
							(forRecording ? "recording" : "playback"));
					
				}
				
			}

			Mixer.Info [] dummy = new Mixer.Info[0];
			return rightTypes.toArray(dummy);			
		}
	}
}