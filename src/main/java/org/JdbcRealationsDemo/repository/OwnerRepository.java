package org.JdbcRealationsDemo.repository;

import org.JdbcRealationsDemo.entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

@Repository
public class OwnerRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void recreateTable(){
        execSQL("DROP TABLE OWNER IF EXISTS");
        execSQL("CREATE TABLE OWNER(id SERIAL, name VARCHAR(255))");
    }


    public void execSQL(String query){
        System.out.println(query);
        jdbcTemplate.execute(query);
    }

    public int checkTableOnExist(){

        String sql = "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = 'OWNER' and TABLE_TYPE='TABLE';";
        System.out.println(sql);
        Integer count = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class);

        return count;
    }


    public List<Owner> findAll(){
        String sqlFindAll = "SELECT * from Owner";
        System.out.println(sqlFindAll);
        return jdbcTemplate.query(
                sqlFindAll,
                new OwnerMapper()
        );
    }


    public Owner findOneByName(String name) {

        String sqlFindByName = "SELECT * from Owner where name = ?";
        System.out.println(sqlFindByName);
        return jdbcTemplate.queryForObject(
                sqlFindByName,
                new Object[]{name},
                new OwnerMapper()
        );
    }

    public Owner create(Owner owner){
        String sqlCreate = "Insert into Owner (name) values (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        System.out.println(sqlCreate);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, owner.getName());
            return ps;
        }, holder);

        int newUserId = holder.getKey().intValue();
        owner.setId(newUserId);
        return owner;
    }

    public void save(Owner owner) {
        String sql = "Update Owner set name=? where id = ?";
        System.out.println(sql);
        jdbcTemplate.update(sql, owner.getName(), owner.getId());
    }

    public void delete(Owner owner) {
        String sql = "Delete from Owner where id = ?";
        System.out.println(sql);
        jdbcTemplate.update(sql, owner.getId());
    }

    public Integer count() {
        return this.jdbcTemplate.queryForObject("select count(id) from Owner", Integer.class);
    }

    public void deleteAll(List<Owner> owners) {
        this.jdbcTemplate.batchUpdate(
            "Delete from Owner where id = ?",
            new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, owners.get(i).getId());
                }
                public int getBatchSize() {
                    return owners.size();
                }
            });

    }

    public List<Owner> findByNames(String... nameArray) {
        String[] newStringArray = new String[nameArray.length];
        for(int i=0; i<nameArray.length; i++){
            newStringArray[i]="\'"+nameArray[i]+"\'";
        }
        String names = Arrays.toString(newStringArray).replace("[", "").replace("]", "");
        String sqlFindAll = "SELECT * from Owner o where o.name in ("+names+")";
        System.out.println(sqlFindAll);
        return jdbcTemplate.query(
                sqlFindAll,
                new OwnerMapper()
        );
    }

    public void deleteAll() {
        String sql = "Delete from Owner";
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }


    public void dropTable(){
        execSQL("DROP TABLE OWNER IF EXISTS");
    }

    private class OwnerMapper implements RowMapper<Owner> {

        public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
            Owner owner = new Owner();
            owner.setId(rs.getLong("id"));
            owner.setName(rs.getString("name"));
            return owner;
        }
    }


}
