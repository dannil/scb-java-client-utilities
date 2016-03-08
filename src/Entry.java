

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Entry {

	private String id;
	private String text;
	private List<Entry> children;

	public Entry() {
		this.children = new ArrayList<Entry>();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Entry> getChildren() {
		return this.children;
	}

	public void setChildren(List<Entry> children) {
		this.children = children;
	}

	public void addChild(Entry entry) {
		this.children.add(entry);
	}

	public void addChildren(Collection<Entry> entries) {
		this.children.addAll(entries);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Entry [id=");
		builder.append(this.id);
		builder.append(", text=");
		builder.append(this.text);
		builder.append(", children=");
		builder.append(this.children);
		builder.append("]");
		return builder.toString();
	}

}
