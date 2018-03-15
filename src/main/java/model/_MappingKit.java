package model;
/**
 * Created by admin on 2018/2/2.
 */
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * 
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("beeeye_host", "host_ids", Host.class);
		arp.addMapping("common_user", "ids", User.class);
		arp.addMapping("common_menu", "ids", Menu.class);
		arp.addMapping("common_setting", "ids", Setting.class);
		arp.addMapping("common_software_group", "ids", SoftwareInstall.class);
		arp.addMapping("beeeye_pelf", "ids", Pelf.class);
		arp.addMapping("beeneedle_process_subject", "ids", ProcessSubject.class);
		arp.addMapping("beeneedle_object_label", "ids", ObjectLabel.class);
		arp.addMapping("beeneedle_process_host", "ids", ProcessHost.class);
		arp.addMapping("beeneedle_object_host", "ids", ObjectHost.class);
		arp.addMapping("beeneedle_integrity", "ids", Integrity.class);
		arp.addMapping("beeeye_baseline_win", "ids", WindowsSafeLib.class);
		arp.addMapping("beeeye_baseline_linux", "ids", LinuxSafeLib.class);
		arp.addMapping("beeeye_host_threshold", "ids", Threshold.class);
		arp.addMapping("beeneedle_mac", "ids", Mac.class);
	}
}
