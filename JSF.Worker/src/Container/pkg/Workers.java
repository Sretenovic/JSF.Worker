package Container.pkg;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import Objects.pkg.*;

@SessionScoped
@ManagedBean(name="workers")
public class Workers {

	private static Workers workers_ton = null; 
	private ArrayList<Worker> workers = new ArrayList<Worker>();

	private Workers() {}

	public static Workers getWorkersInstance() { if(workers_ton == null) workers_ton = new Workers(); return workers_ton; }

	public void setWorkers(Worker worker) { workers.add(worker); }
	public ArrayList<Worker> getWorkers(){ return workers; }
	public void clearWorkers() { workers.clear(); }
	public void removeWorker(Worker worker) { workers.remove(worker); }

}
