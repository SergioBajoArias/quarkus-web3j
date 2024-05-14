// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.25;

contract ERC20 {
    uint amount = 0;

    function mint(uint mintedAmount) public {
        amount += mintedAmount;
    }

    function balanceOf() external view returns (uint) {
        return amount;
    }
}