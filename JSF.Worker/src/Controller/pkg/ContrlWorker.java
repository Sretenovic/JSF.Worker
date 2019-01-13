package Controller.pkg;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import Container.pkg.Workers;
import Database.pkg.DB;
import Objects.pkg.Worker;


@ManagedBean(name="CWorker")
public class ContrlWorker {

	private Worker steve;
	private DB db = DB.getDBBClass();
	private Workers workers = Workers.getWorkersInstance();

	//Constructor
	public ContrlWorker(Worker model, DB broker) { this.steve = model; this.db = broker; }
	public ContrlWorker() { if(this.steve == null) this.steve = new Worker(); }

	//DB
	public DB getDBBClass() { return db; }

	//Workers
	public void setWorkers(Worker steve) { workers.setWorkers(steve); }
	public ArrayList<Worker> getWorkers() { return workers.getWorkers(); }
	public void removeWorker(Worker worker) { workers.removeWorker(worker); }

	//Worker steve
	public String getPersonsID() { return steve.getPersonsID(); }
	public void setPersonsID(String personsID) { steve.setPersonsID(personsID); }

	public String getName() { return steve.getName(); }
	public void setName(String name) { steve.setName(name); }

	public String getLastName() { return steve.getLastName(); }
	public void setLastName(String lastname) { steve.setLastName(lastname); }
	
	public String getSomeAction() { return steve.getSomeAction(); }
	public void setAction(String action) { steve.setAction(action); }

	private boolean checkWorker() { 
		if(!getPersonsID().equals("") && !getName().equals("") && !getLastName().equals("")) return true; return false; }

	///////Worker: insert array fill
	public void insertArray() {
		if(checkWorker()) { setAction("insert"); setWorkers(steve); steve = new Worker(); } 
		else {
			FacesContext currInst = FacesContext.getCurrentInstance();
			currInst.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Parameters:", "Please insert fill all fields."));   
		}
	}

	///////Worker: update array fill
	public void updateArray() {
		if(checkWorker()) { setAction("update"); setWorkers(steve); steve = new Worker(); }
		else {
			FacesContext currInst = FacesContext.getCurrentInstance();
			currInst.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Parameters!", "Please insert fill all fields."));   
		}
	}

	///////Worker: delete array fill
	public void deleteArray() {
		if(!steve.getPersonsID().equals("")) { setAction("delete"); setWorkers(steve); steve = new Worker(); }
		else {
			FacesContext currInst = FacesContext.getCurrentInstance();
			currInst.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Parameters!", "Please insert Person's ID."));   
		}
	}

	//Commit - DB actions
	public void IUD() throws SQLException {
		FacesContext currInst = FacesContext.getCurrentInstance();
		int ins = 0, upd = 0, del = 0, all = workers.getWorkers().size(); db.setMsg("");

		if(!workers.getWorkers().isEmpty()) {
			try {

				for(Worker worker : workers.getWorkers()) {
						
					if(worker.getSomeAction()=="insert") { db.insWorkerDB(worker);
					if(db.getMsg().equals("")) { ins = ins + 1; db.commitTransaction(); continue; }
					else { db.rollbackTransaction(); currInst.addMessage(null, new FacesMessage("Error: ", db.getMsg())); }}

					if(worker.getSomeAction()=="update") { db.updWorkerDB(worker);
					if(db.getMsg().equals("")) { upd = upd + 1; db.commitTransaction(); continue; } 
					else { db.rollbackTransaction(); currInst.addMessage(null, new FacesMessage("Error: ", db.getMsg())); }}

					if(worker.getSomeAction()=="delete") { db.delWorkerDB(worker);
					if(db.getMsg().equals("")) { del = del + 1; db.commitTransaction(); continue; } 
					else { db.rollbackTransaction(); currInst.addMessage(null, new FacesMessage("Error: ", db.getMsg())); }}
				}
			} catch (SQLException e) { e.printStackTrace();
			} finally { if(db.getMsg().equals("")) currInst.addMessage(null, new FacesMessage("Finished: - Insert: "+ins+" Update: "+upd+" Delete: "+del+" ALL: "+all));
			db.breakConnection(); workers.getWorkers().clear();  }
		} else { currInst.addMessage(null, new FacesMessage("Error: there is no Workers to be inserted, updated or deleted!", db.getMsg())); }
	}


}



