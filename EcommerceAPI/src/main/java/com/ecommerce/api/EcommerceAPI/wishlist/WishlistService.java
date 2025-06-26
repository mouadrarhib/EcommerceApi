package com.ecommerce.api.EcommerceAPI.wishlist;

import com.ecommerce.api.EcommerceAPI.helpers.AuthHelper;
import com.ecommerce.api.EcommerceAPI.mappers.WishlistMapper;
import com.ecommerce.api.EcommerceAPI.product.Product;
import com.ecommerce.api.EcommerceAPI.product.ProductService;
import com.ecommerce.api.EcommerceAPI.user.User;
import com.ecommerce.api.EcommerceAPI.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final WishlistMapper wishlistMapper;

    public WishlistService(WishlistRepository wishlistRepository,
                           WishlistItemRepository wishlistItemRepository,
                           UserRepository userRepository,
                           ProductService productService,
                           WishlistMapper wishlistMapper) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.wishlistMapper = wishlistMapper;
    }

    @Transactional
    public Wishlist getOrCreateWishlist() {
        Integer userId = AuthHelper.getCurrentAuthenticatedUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist(user);
                    return wishlistRepository.save(newWishlist);
                });
    }

    @Transactional
    public WishlistResponse addItemToWishlist(UUID productId) {
        Wishlist wishlist = getOrCreateWishlist();
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (wishlistItemRepository.findByWishlistAndProduct(wishlist, product).isPresent()) {
            throw new IllegalArgumentException("Product already exists in the wishlist.");
        }

        WishlistItem wishlistItem = new WishlistItem(wishlist, product);
        wishlist.addWishlistItem(wishlistItem); // Use the helper method
        wishlistItemRepository.save(wishlistItem);
        return mapWishlistToResponse(wishlist);
    }

    @Transactional
    public WishlistResponse removeItemFromWishlist(UUID productId) {
        Wishlist wishlist = getOrCreateWishlist();
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        WishlistItem wishlistItem = wishlistItemRepository.findByWishlistAndProduct(wishlist, product)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in the wishlist."));

        wishlistItemRepository.delete(wishlistItem);
        return mapWishlistToResponse(wishlist);
    }

    public Optional<WishlistResponse> getWishlist() {
        Integer userId = AuthHelper.getCurrentAuthenticatedUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        return wishlistRepository.findByUserIdWithItems(userId)
                .map(this::mapWishlistToResponse);
    }

    private WishlistResponse mapWishlistToResponse(Wishlist wishlist) {
        System.out.println("Mapping wishlist: " + wishlist.getId());
        System.out.println("Number of items: " + (wishlist.getWishlistItems() != null ? wishlist.getWishlistItems().size() : 0));

        WishlistResponse response = wishlistMapper.toResponse(wishlist);

        Set<WishlistItemResponse> items = wishlist.getWishlistItems().stream()
                .peek(item -> System.out.println("Processing item: " + item.getId() +
                        ", product: " + (item.getProduct() != null ? item.getProduct().getId() : "null")))
                .map(wishlistMapper::toItemResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        response.setItems(items);
        return response;
    }
}