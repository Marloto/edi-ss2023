package de.thi.informatik.edi.pubsub;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleChangerManager extends ChangeManager {
	private Map<Channel, List<Observer>> map;
	
	public SimpleChangerManager() {
		map = new HashMap<>();
	}

	@Override
	public void register(Channel sub, Observer obs) {
		List<Observer> value;
		if(!this.map.containsKey(sub)) {
			value = new LinkedList<>();
			this.map.put(sub, value);
		} else {
			value = this.map.get(sub);
		}
		value.add(obs);
	}

	@Override
	public void notify(Channel sub, String message) {
		if(this.map.containsKey(sub)) {
			for(Observer obs : this.map.get(sub)) {
				obs.update(message);
			}
		}
	}
	
}
