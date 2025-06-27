package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.entity.Address;
import com.example.ShopAppEcomere.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {

   private final AddressService addressService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<Address> getAllAddress() {
        return addressService.getAddressList();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<Address> getAddressById(@PathVariable("id") Integer id) {
        return addressService.getAddressById(id);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Address createAddress(@RequestBody Address address) {
        return addressService.create(address);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Address update(@PathVariable Integer id, @RequestBody Address address) throws Exception {
        return addressService.update(id, address);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void delete(@PathVariable Integer id) {
        addressService.delete(id);
    }

    @PutMapping("/user/{id}/{idAddress}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Address changeDefault(@PathVariable Integer id, @PathVariable Integer idAddress) throws Exception {
        return addressService.changeDefault(id, idAddress);
    }
}
