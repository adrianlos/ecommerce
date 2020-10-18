package com.github.skorczan.ecommerce.configuration;

import com.github.skorczan.ecommerce.domain.Author;
import com.github.skorczan.ecommerce.domain.AuthorRepository;
import com.github.skorczan.ecommerce.domain.Product;
import com.github.skorczan.ecommerce.domain.ProductCategory;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import com.github.skorczan.ecommerce.domain.ProductRepository;
import com.github.skorczan.ecommerce.domain.ProductType;
import com.github.skorczan.ecommerce.domain.User;
import com.github.skorczan.ecommerce.domain.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Profile({"sample-data", "test"})
@Component
@Transactional
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class SampleDataFixture {

    @Getter(AccessLevel.NONE)
    private final AuthorRepository authorRepository;

    @Getter(AccessLevel.NONE)
    private final ProductCategoryRepository productCategoryRepository;

    @Getter(AccessLevel.NONE)
    private final ProductRepository productRepository;

    @Getter(AccessLevel.NONE)
    private final UserRepository userRepository;

    @Getter(AccessLevel.NONE)
    private final EntityManager entityManager;

    private Author janKowalski;
    private Author karolinaNowak;
    private ProductCategory clothes;
    private ProductCategory childClothes;
    private ProductCategory manClothes;
    private ProductCategory womanClothes;
    private ProductCategory drinks;
    private ProductCategory drinks2;
    private ProductCategory stillDrinks;
    private ProductCategory fizzyDrinks;
    private Product szmata;
    private Product buty;
    private User admin;
    private User customer;

    public boolean shouldBeSaved() {
        return authorRepository.count() == 0 &&
                productCategoryRepository.count() == 0 &&
                productRepository.count() == 0;
    }

    public void save() {
        janKowalski = generateAuthor("Jan Kowalski");
        karolinaNowak = generateAuthor("Karolina Nowak");

        clothes = generateCategory("Odzież", null);
        childClothes = generateCategory("Odzież dziecięca", clothes);
        manClothes = generateCategory("Odzież męska", clothes);
        womanClothes = generateCategory("Odzież damska", clothes);

        drinks = generateCategory("Napoje", null);
        drinks2 = generateCategory("Napoje alkoholowe", drinks);
        stillDrinks = generateCategory("Napoje niegazowane", drinks);
        fizzyDrinks = generateCategory("Napoje gazowane", drinks);

        szmata = productRepository.save(szmataBuilder().build());
        buty = productRepository.save(butyBuilder().build());

        for(int i = 0; i < 20; ++i) {
            productRepository.save(szmataBuilder().build());
            productRepository.save(butyBuilder().build());
        }

        admin = userRepository.save(User.builder()
                .login("admin@example.com")
                .password("$2a$10$.914oSZ08ODTT6AEJBiSkOkE92yYNoVHQ8.gO/vfYHBKm6QLw18ty")
                .role(User.Role.ADMIN)
                .contactPreference(User.ContactPreference.EMAIL)
                .avatarUrl("http://google.com")
                .country("NOWHERE")
                .city("NOWHERE")
                .street("HOMELESS")
                .zipCode("00-000")
                .build());

        customer = userRepository.save(User.builder()
                .login("customer@example.com")
                .password("$2a$10$NGolOKBD8wps782P43H5GuSzhiWytmsHifhi5VC3GaZqiLScy0VCa")
                .role(User.Role.CUSTOMER)
                .contactPreference(User.ContactPreference.EMAIL)
                .avatarUrl("http://google.com")
                .country("NOWHERE")
                .city("NOWHERE")
                .street("HOMELESS")
                .zipCode("00-000")
                .build());
    }

    private Product.ProductBuilder szmataBuilder() {
        return Product.builder()
                .title("Szmata")
                .description("Stara, podarta, ale lepsza niż nic")
                .thumbnailUrl("https://0.allegroimg.com/original/015d9b/2918b4954b368e3c63f3e24d6140/SCIERKA-PODLOGOWA-SZMATA-DO-PODLOGI-CHLONNA-AGD")
                .price(ThreadLocalRandom.current().nextDouble(1, 100))
                .author(janKowalski)
                .category(clothes)
                .type(ProductType.CLOTHES);
    }

    private Product.ProductBuilder butyBuilder() {
        return Product.builder()
                .title("Buty")
                .description("Lepsze to niż chodzić na bosaka")
                .thumbnailUrl("https://i.imgur.com/3kfa7nQ.jpg")
                .price(ThreadLocalRandom.current().nextDouble(1, 100))
                .author(janKowalski)
                .category(clothes)
                .type(ProductType.CLOTHES);
    }

    public void remove() {
        entityManager.clear();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Author generateAuthor(String fullName) {
        String[] nameParts = fullName.split(" ");

        if (nameParts.length != 2) {
            throw new IllegalArgumentException("fullName not in correct format");
        }

        Author result = new Author();
        result.setFirstName(nameParts[0]);
        result.setLastName(nameParts[1]);
        return authorRepository.save(result);
    }

    private ProductCategory generateCategory(String name, ProductCategory parentCategory) {
        ProductCategory result = new ProductCategory();
        result.setName(name);
        result.setParentCategory(parentCategory);
        return productCategoryRepository.save(result);
    }
}
