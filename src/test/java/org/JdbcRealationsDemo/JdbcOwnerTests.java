package org.JdbcRealationsDemo;

import org.JdbcRealationsDemo.entity.Owner;
import org.JdbcRealationsDemo.entity.Pet;
import org.JdbcRealationsDemo.repository.OwnerRepository;
import org.JdbcRealationsDemo.repository.PetRepository;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JdbcRealationsDemoApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcOwnerTests{

	@Autowired
	PetRepository petRepository;

	@Autowired
	OwnerRepository ownerRepository;

	@Test
	public void test01_createTable(){
		ownerRepository.recreateTable();
		Assert.assertEquals(1, ownerRepository.checkTableOnExist());


	}

	@Test
	public void test02_createSecondTable(){
		petRepository.recreateTable();
		Assert.assertEquals(1, petRepository.checkTableOnExist());
	}



	@Test
	public void test03_fillTableAndFindAll() throws InterruptedException {
		List<Owner> owners = Arrays.asList(new Owner("Вася"), new Owner("Коля"), new Owner("Петя"), new Owner("Марина"));
		for (Owner owner:owners) {
			ownerRepository.create(owner);
		}



		System.out.println("owners = "+owners);
		Assert.assertEquals(4, ownerRepository.findAll().size());
	}

	@Test
	public void test04_createOwnerWithPetsAndFindAll() throws InterruptedException {
		List<Owner> ownersWithPets = Arrays.asList(new Owner("Алексей").addPet(new Pet("Ася")).addPet(new Pet("Мотя")));

		for (Owner owner:ownersWithPets) {
			ownerRepository.createWithPets(owner);
		}

		List<Owner> ownersWithPetsFromDB = ownerRepository.findAllWithPets();
		Assert.assertEquals(5,ownersWithPetsFromDB.size());

		Owner owner = ownersWithPetsFromDB.get(4);

		Pet pet1 = owner.getPets().get(0);
		Pet pet2 = owner.getPets().get(1);

		Assert.assertEquals("Ася",pet1.getName());
		Assert.assertEquals("Мотя",pet2.getName());
	}


	@Test
	public void test05_findAndUpdate(){
		String name = "Вася";
		Owner owner = ownerRepository.findOneByName(name);
		Assert.assertEquals(name, owner.getName());

		String newName = "Василий";
		owner.setName(newName);
		ownerRepository.save(owner);
		Owner updatedOwner = ownerRepository.findOneByName(newName);
		Assert.assertEquals(newName, updatedOwner.getName());
	}


	@Test
	public void test06_removeAndCount(){
		String name = "Василий";
		Owner owner = ownerRepository.findOneByName(name);
		Assert.assertEquals(name, owner.getName());

		ownerRepository.delete(owner);

		Assert.assertEquals(4, ownerRepository.count().intValue());

	}

	@Test
	public void test07_removeListOfRecords(){
		List<Owner> owners = ownerRepository.findByNames("Петя", "Марина");
		Assert.assertEquals(2, owners.size());
		Assert.assertTrue(owners.stream().anyMatch(owner -> owner.getName().equals("Петя")));
		Assert.assertTrue(owners.stream().anyMatch(owner -> owner.getName().equals("Марина")));

		ownerRepository.deleteAll(owners);
		Assert.assertEquals(2, ownerRepository.count().intValue());

	}



	@Test
	public void test08_removeAllAndDropTable(){
		ownerRepository.deleteAll();
		Assert.assertEquals(0, ownerRepository.count().intValue());
		ownerRepository.dropTable();
		Assert.assertEquals(0, ownerRepository.checkTableOnExist());
		petRepository.deleteAll();
		Assert.assertEquals(0, petRepository.count().intValue());
		petRepository.dropTable();
		Assert.assertEquals(0, petRepository.checkTableOnExist());
	}
}
