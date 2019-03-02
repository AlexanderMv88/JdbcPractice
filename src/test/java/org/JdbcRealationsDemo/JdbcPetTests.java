package org.JdbcRealationsDemo;

import org.JdbcRealationsDemo.entity.Pet;
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
public class JdbcPetTests implements JdbcEntityTests{

	@Autowired
	PetRepository petRepository;

	@Test
	public void test01_createTable(){
		petRepository.recreateTable();
		Assert.assertEquals(1, petRepository.checkTableOnExist());
	}


	@Test
	public void test02_fillTableAndFindAll() throws InterruptedException {
		List<Pet> pets = Arrays.asList(new Pet("Вольт"), new Pet("Блэк"), new Pet("Стэла"), new Pet("Мурзик"));
		for (Pet pet:pets) {
			petRepository.create(pet);
		}
		System.out.println("pets = "+pets);
		Assert.assertEquals(4, petRepository.findAll().size());
	}


	@Test
	public void test03_findAndUpdate(){
		String name = "Мурзик";
		Pet pet = petRepository.findOneByName(name);
		Assert.assertEquals(name, pet.getName());

		String newName = "Мурзач";
		pet.setName(newName);
		petRepository.save(pet);
		Pet updatedPet = petRepository.findOneByName(newName);
		Assert.assertEquals(newName, updatedPet.getName());
	}


	@Test
	public void test04_removeAndCount(){
		String name = "Мурзач";
		Pet pet = petRepository.findOneByName(name);
		Assert.assertEquals(name, pet.getName());

		petRepository.delete(pet);

		Assert.assertEquals(3, petRepository.count().intValue());

	}

	@Test
	public void test05_removeListOfRecords(){
		List<Pet> pets = petRepository.findByNames("Вольт", "Стэла");
		Assert.assertEquals(2, pets.size());
		Assert.assertTrue(pets.stream().anyMatch(pet -> pet.getName().equals("Вольт")));
		Assert.assertTrue(pets.stream().anyMatch(pet -> pet.getName().equals("Стэла")));

		petRepository.deleteAll(pets);
		Assert.assertEquals(1, petRepository.count().intValue());

	}



	@Test
	public void test06_removeAllAndDropTable(){
		petRepository.deleteAll();
		Assert.assertEquals(0, petRepository.count().intValue());
		petRepository.dropTable();
		Assert.assertEquals(0, petRepository.checkTableOnExist());

	}
}
