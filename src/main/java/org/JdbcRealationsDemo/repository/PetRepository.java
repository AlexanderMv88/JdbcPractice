package org.JdbcRealationsDemo.repository;

import org.JdbcRealationsDemo.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@Repository
public class PetRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void recreateTable(){
        execSQL("DROP TABLE PET IF EXISTS");
        execSQL("CREATE TABLE PET(id SERIAL, name VARCHAR(255), owner_id LONG)");
    }


    public void execSQL(String query){
        System.out.println(query);
        jdbcTemplate.execute(query);
    }

    public int checkTableOnExist(){

        String sql = "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = 'PET' and TABLE_TYPE='TABLE';";
        System.out.println(sql);
        Integer count = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class);

        return count;
    }


    public List<Pet> findAll(){
        String sqlFindAll = "SELECT * from Pet";
        System.out.println(sqlFindAll);
        return jdbcTemplate.query(
                sqlFindAll,
                new PetMapper()
        );
    }


    public Pet findOneByName(String name) {

        String sqlFindByName = "SELECT * from Pet where name = ?";
        System.out.println(sqlFindByName);
        return jdbcTemplate.queryForObject(
                sqlFindByName,
                new Object[]{name},
                new PetMapper()
        );
    }

    public Pet create(Pet pet){
        String sqlCreate = "Insert into Pet (name, owner_id) values (?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        System.out.println(sqlCreate);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pet.getName());
            ps.setLong(2, pet.getOwnerId());
            return ps;
        }, holder);

        int newUserId = holder.getKey().intValue();
        pet.setId(newUserId);
        return pet;
    }

    public void save(Pet owner) {
        String sql = "Update Pet set name=? where id = ?";
        System.out.println(sql);
        jdbcTemplate.update(sql, owner.getName(), owner.getId());
    }

    public void delete(Pet owner) {
        String sql = "Delete from Pet where id = ?";
        System.out.println(sql);
        jdbcTemplate.update(sql, owner.getId());
    }

    public Integer count() {
        return this.jdbcTemplate.queryForObject("select count(id) from Pet", Integer.class);
    }

    public void deleteAll(List<Pet> owners) {
        this.jdbcTemplate.batchUpdate(
            "Delete from Pet where id = ?",
            new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, owners.get(i).getId());
                }
                public int getBatchSize() {
                    return owners.size();
                }
            });

    }

    public List<Pet> findByNames(String... nameArray) {
        String[] newStringArray = new String[nameArray.length];
        for(int i=0; i<nameArray.length; i++){
            newStringArray[i]="\'"+nameArray[i]+"\'";
        }
        String names = Arrays.toString(newStringArray).replace("[", "").replace("]", "");
        String sqlFindAll = "SELECT * from Pet o where o.name in ("+names+")";
        System.out.println(sqlFindAll);
        return jdbcTemplate.query(
                sqlFindAll,
                new PetMapper()
        );
    }

    public void deleteAll() {
        String sql = "Delete from Pet";
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }


    public void dropTable(){
        execSQL("DROP TABLE PET IF EXISTS");
    }

    public List<Pet> findAllByOwnerId(long ownerId) {

        String sqlFindByName = "SELECT * from Pet where owner_id = "+ownerId;

        System.out.println(sqlFindByName);
        return jdbcTemplate.query(
                sqlFindByName,
                new PetMapper()
        );

    }

    private class PetMapper implements RowMapper<Pet> {

        public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pet owner = new Pet();
            owner.setId(rs.getLong("id"));
            owner.setName(rs.getString("name"));
            return owner;
        }
    }


}
