package com.ausland.weixin.model.db;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
 

@Entity
@Table(name="category")
public class Category {

    @Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer categoryId;
    
    @Column(length = 128)	
    String CategoryName;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", CategoryName=" + CategoryName + "]";
	}
     

}
