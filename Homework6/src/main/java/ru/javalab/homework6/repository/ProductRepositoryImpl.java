package ru.javalab.homework6.repository;

import ru.javalab.homework6.models.Product;
import ru.javalab.homework6.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements CrudRepository<Product> {
    private static Connection connection;
    private static ProductRepositoryImpl productRepository;

    private ProductRepositoryImpl() {
        DBConnection.setDbPropFilePath("C:\\Users\\Vladislav\\IdeaProjects\\JavaLab\\Homework6\\src\\main\\java\\ru\\javalab\\homework6\\bin\\db.properties");
        connection = new DBConnection().getConnection();
    }

    RowMapper<Product> mapper = rs -> {
        try {
            return new Product(rs.getInt("id"), rs.getInt("price"), rs.getString("name"));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public static ProductRepositoryImpl getInstance() {
        return productRepository == null ? productRepository = new ProductRepositoryImpl() : productRepository;
    }

    @Override
    public Product add(Product product) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO product (name, price) VALUES (?,?)")) {
            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getPrice());
            stmt.executeUpdate();
            return product;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<Product> find(Product product) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE name = ?")) {
            stmt.setString(1, product.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return Optional.empty();
    }

    public Integer getIdByName(Product product) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM product WHERE name = ?")) {
            stmt.setString(1, product.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            else
                return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Product> getAllProductsByUser(User user) {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE id = ANY (SELECT product_id FROM product_to_user WHERE user_id = ?)")) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void addOrder(User user, Product product) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO product_to_user (user_id, product_id) VALUES (?,?)")) {
            stmt.setInt(1, user.getId());
            stmt.setInt(2, getIdByName(product));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapper.mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(Product product) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE id = ?")) {
            statement.setInt(1, product.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
