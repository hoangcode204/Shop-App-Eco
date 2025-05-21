package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.entity.Address;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAddressList() {
        return addressRepository.findAll();
    }


    public List<Address> getAddressById(Integer id) {
        return addressRepository.findAddressByUserId(id);
    }


    public Address create(Address address) {
        Address addressDefault = addressRepository.findAddressDefaultByUserId(address.getUser().getId());
        address.setIs_default(addressDefault == null);
        return addressRepository.save(address);
    }


    public Address update(Integer id, Address address) throws Exception {
        Address tempAddress = addressRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
        tempAddress.setFullname(address.getFullname());
        tempAddress.setPhone(address.getPhone());
        tempAddress.setCity(address.getCity());
        tempAddress.setDistrict(address.getDistrict());
        tempAddress.setWards(address.getWards());
        tempAddress.setSpecific_address(address.getSpecific_address());
        return addressRepository.save(tempAddress);
    }


    public void delete(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        if (Boolean.TRUE.equals(address.getIs_default())) {
            throw new AppException(ErrorCode.CANNOT_DELETE_DEFAULT_ADDRESS);
        }

        addressRepository.deleteById(id);
    }

    public Boolean existsById(Integer id) {
        return addressRepository.existsById(id);
    }

    public Address changeDefault(Integer id, Integer idAddress) {
        Address addressDefault = addressRepository.findAddressDefaultByUserId(id);

        if (addressDefault == null) {
            throw new AppException(ErrorCode.ADDRESS_DEFAULT_NOT_FOUND);
        }

        addressDefault.setIs_default(false);
        addressRepository.save(addressDefault);

        Address newDefaultAddress = addressRepository.findById(idAddress)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        if (Boolean.TRUE.equals(newDefaultAddress.getIs_default())) {
            throw new AppException(ErrorCode.ADDRESS_ALREADY_DEFAULT);
        }

        newDefaultAddress.setIs_default(true);
        return addressRepository.save(newDefaultAddress);
    }

}
