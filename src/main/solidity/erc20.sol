// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.25;

contract ERC20 {
    uint amount = 0;

    function mint(uint mintedAmount) public {
        amount += mintedAmount;
    }

    function burn(uint burnAmount) public {
        require(burnAmount <= amount, "There aren't enough units to be burnt");
        amount -= burnAmount;
    }

    function balanceOf() external view returns (uint) {
        return amount;
    }
}