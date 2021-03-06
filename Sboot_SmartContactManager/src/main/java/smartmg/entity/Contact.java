package smartmg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "contacts")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cId;
	
	private String name;
	private String secondName;
	private String work;
	private String email; 
	private long phone;
	private String image;
	
	@Column(length = 5000)
	private String description;
	
	@ManyToOne
	@JsonIgnore					// Iss Annotaion ke wajah se JSON ka data "user" field ke liye serialize nhi hoga, nhi to circular dependency ke case me fasa reh jayega. 
	private User user;
	
	public Contact() {
		super();
	}
	
	public Contact(int cId, String name, String secondName, String work, String email, long phone, String image,
			String description, User user) {
		super();
		this.cId = cId;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
		this.user = user;
	}

	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Contact [cId= " + cId + ", name= " + name + ", secondName= " + secondName + ", work= " + work + ", email= "
				+ email + ", phone= " + phone + ", image= " + image;
	}

	// Sare Lists se 1 particular contact ko remove karne ke liye "Object Matching" ki zarurat padegi.
	@Override
	public boolean equals(Object obj) {
		return this.cId==((Contact)obj).getcId();
	}
	

	
	
}
