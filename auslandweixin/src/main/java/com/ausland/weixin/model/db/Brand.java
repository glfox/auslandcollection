package com.ausland.weixin.model.db;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 

@Entity
@Table(name="brand")
public class Brand {
    
    @Id
    @Column(length = 128)	
    String brandName;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
	public String toString() {
		return "Brand [brandName=" + brandName + "]";
	}

}
