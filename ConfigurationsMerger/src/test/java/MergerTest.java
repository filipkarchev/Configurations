import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import builder.ConfigurationsBuilder;
import builder.MergerType;
import builder.OrderManager;
import config.Configuration;
import config.ConfigurationStatus;
import fr.familiar.variable.FeatureModelVariable;

public class MergerTest {
	

	@Test
	public void testTree() {
		ConfigurationsBuilder builder = new ConfigurationsBuilder(MergerType.TREE,"signal_android_tested.xml","tested.bin");
		HashMap<String,List<String>> list = new HashMap<String,List<String>>();
		
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("pref_timeout_passphrase_false");
		list.put("pref_timeout_passphrase", list1);
		
		
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("pref_key_enable_notifications_true");
		list.put("pref_key_enable_notifications", list2);
		
		
		Configuration newConf = new Configuration();
		newConf.setFeatures(list);
		builder.addTestedConfiguration(newConf);
		
		assertEquals(ConfigurationStatus.TESTED, builder.determineConfigurationStatus(newConf));
		
	}
	
	@Test
	public void testFam() {
		ConfigurationsBuilder builder = new ConfigurationsBuilder(MergerType.FM,"signal_android_tested.xml","tested.fam");
		HashMap<String,List<String>> list = new HashMap<String,List<String>>();
		
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("pref_timeout_passphrase_false");
		list.put("pref_timeout_passphrase", list1);
		
		
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("pref_system_emoji_false");
		list.put("pref_system_emoji", list2);
		
		
		Configuration newConf = new Configuration();
		newConf.setFeatures(list);
		System.out.println("Added: " + builder.addTestedConfiguration(newConf));
		
		assertEquals(ConfigurationStatus.TESTED, builder.determineConfigurationStatus(newConf));
		
	}

}
