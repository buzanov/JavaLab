package ru.javalab.homework7.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.javalab.homework7.models.Product;
import ru.javalab.homework7.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductRepositoryImpl implements CrudRepository<Product> {
    private static Connection connection = new DBConnection().getConnection();
    private JdbcTemplate template;
    Map<Integer, Product> map = new HashMap<>();

    public static final String SQL_ADD_PRODUCT = "INSERT INTO product (name, price) VALUES (?,?)";
    public static final String SQL_FIND_ALL = "SELECT * FROM product";
    public static final String SQL_FIND_BY_NAME = "SELECT * FROM product WHERE name = ?";
    public static final String SQL_FIND_ALL_PRODUCT_BY_USER = "SELECT * FROM product WHERE id = ANY (SELECT product_id FROM product_to_user WHERE user_id = ?)";
    public static final String SQL_ADD_ORDER = "INSERT INTO product_to_user (user_id, product_id) VALUES (?,?)";
    public static final String SQL_FIND_BY_ID = "SELECT * FROM product WHERE id = ?";
    public static final String SQL_DELETE_BY_ID = "DELETE FROM product WHERE id = ?";

    RowMapper<Product> mapper = (rs, i) -> {
        try {
            Integer id = rs.getInt("id");
            map.put(id, new Product(id, rs.getInt("price"), rs.getString("name")));
            return map.get(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    @Override
    public Product add(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(SQL_ADD_PRODUCT, new Object[]{product.getName(), product.getPrice()}, keyHolder);
        product.setId(keyHolder.getKey().intValue());
        return product;
    }

    @Override
    public Optional<Product> find(Product product) {
        return Optional.of(template.queryForObject(SQL_FIND_BY_NAME, mapper, product.getName()));
    }

    public Integer getIdByName(Product product) {
        if (find(product).isPresent())
            return find(product).get().getId();
        else return null;
    }

    public List<Product> getAllProducts() {
        return template.query(SQL_FIND_ALL, mapper);
    }

    public List<Product> getAllProductsByUser(User user) {
        return template.query(SQL_FIND_ALL_PRODUCT_BY_USER, new Object[]{user.getId()}, mapper);
    }

    public void addOrder(User user, Product product) {
        template.update(SQL_ADD_ORDER, user.getId(), product.getId());
    }

    public Product getProductById(int id) {
        return template.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, mapper);
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(Product product) {
        return template.update(SQL_DELETE_BY_ID, product.getId()) == 1;
    }

}
