// this class handle the threads I created.
// watching the input folder etc..
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.Keymap;



public class Watcher implements Runnable{
	
	String path;
	
	public Watcher(String address)
	{
		this.path=address;
	}
	public void SetPath(String path)
	{
		this.path=path;
	}

	public void run()
	{
		try (WatchService service = FileSystems.getDefault().newWatchService()) {
			Map<WatchKey, Path> KeyMap=new HashMap<>();
			Path path=Paths.get(this.path);
			KeyMap.put(path.register(service, 
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY), path);

			WatchKey watchKey;

			do {
				watchKey=service.take();
				Path eventDir=KeyMap.get(watchKey);

				for(WatchEvent<?> event : watchKey.pollEvents())
				{
					WatchEvent.Kind<?> kind=event.kind();
					Path eventPath= (Path)event.context();
					if(kind.name()=="ENTRY_DELETE" || kind.name()=="ENTRY_CREATE" || kind.name()=="ENTRY_MODIFY")//DO || ADD AND MODIFY
					{
						InputCsv TakeFolder=new InputCsv(this.path);
						myGui.Glist.wiggledata=TakeFolder.importfiles();
						HashMap<position,List<Scan>> m1=new HashMap();
						m1=TakeFolder.makemap(myGui.Glist.wiggledata);
						TakeFolder.makefinaltable(m1);//file name will be guioutput
						MakeKML getData=new MakeKML(myGui.outputpath);
						myGui.Glist.Data=getData.inputthefile();
						myGui.Glist.DataBackUp=myGui.Glist.Data;
					}
					
				}

				Thread.sleep(4000);

			} while (watchKey.reset());


		} catch (IOException | InterruptedException e) {
			
		//	e.printStackTrace();
			//System.out.println("thread1 was interrupted");
		}
	}
}