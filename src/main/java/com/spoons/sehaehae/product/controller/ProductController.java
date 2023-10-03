package com.spoons.sehaehae.product.controller;

import com.spoons.sehaehae.product.dto.CartDTO;
import com.spoons.sehaehae.product.dto.CategoryDTO;
import com.spoons.sehaehae.product.dto.ProductDTO;
import com.spoons.sehaehae.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Value("${image.image-dir}")
    private String IMG_DIR;
    private final MessageSourceAccessor messageSourceAccessor;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService, MessageSourceAccessor messageSourceAccessor) {
        this.productService = productService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @GetMapping("/list")
    public void productList(Model model) {
        List<ProductDTO> productList = productService.selectProduct();
        List<CategoryDTO> categoryList = productService.selectCategory();


        model.addAttribute("productList",productList);
        model.addAttribute("categoryList",categoryList);
    }

    @GetMapping("/productRegist")
    public void registProduct() {

    }

    @GetMapping("/detail")
    public void productDetail(@RequestParam int code, Model model) {
       ProductDTO product=  productService.selectProductByCode(code);
        model.addAttribute("product",product);
    }
    @GetMapping("/cart")
    public void cart(Model model) {
        int i = 1;
        List<CartDTO> cartList = productService.cartList(i);
        System.out.println(cartList);
        model.addAttribute("cartList", cartList);
        model.addAttribute("price",3000);
    }
    @GetMapping("/payment")
    public void payemnt() {
    }
    @GetMapping("/categoryRegist")
    public void categoryRegist() {
    }
    @PostMapping("/categoryRegist")
    public String regist(@RequestParam String categoryName, RedirectAttributes rttr) {
        System.out.println(categoryName);
        productService.registCategory(categoryName);
        rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("category.regist"));

        return "redirect:/product/list";
    }
    @PostMapping("/regist")
    public String productRegist(@ModelAttribute ProductDTO product, @RequestParam(value = "productImage", required = false) MultipartFile productImage) {
        product.setRegistDate(new Date());
        String originalName = productImage.getOriginalFilename();
        String fileUploadDir = IMG_DIR + "thumbnail";
        File dir = new File(fileUploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            if (productImage.getSize() > 0) {
                productImage.transferTo(new File(fileUploadDir +"/"+ originalName));
                product.setPhoto("/upload/thumbnail/"+originalName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productService.registProduct(product);
        return "redirect:/product/list";
    }
    @GetMapping("/getPrice")
    public ResponseEntity<Integer> getPrice(@RequestParam(required = false) int price, @RequestParam(required = false) int body,int eco, int premium){
        System.out.println(price);
        System.out.println(body);
        int total = (price * body)+eco+premium;
        return ResponseEntity.ok(total);
    }

    @GetMapping("/addCart")
    public ResponseEntity<String> addCart(@ModelAttribute CartDTO cart){
        cart.setMember1(1);
        System.out.println("==========================");
        System.out.println(cart.getProductCode1());
        System.out.println(cart.getAmount1());
        System.out.println(cart.getMember1());
        System.out.println(cart.getUseEco1());
        System.out.println(cart.getUsePremium1());


        System.out.println(cart);

//        productService.addCart(cart);

        return ResponseEntity.ok("장바구니에 추가됨");
    }
}
