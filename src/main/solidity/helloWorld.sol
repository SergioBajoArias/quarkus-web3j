// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.25;

contract HelloWorld {
    constructor() {
    }

    function sayHello(string memory name) public pure returns (string memory) {
        return string(abi.encodePacked("Hello ", name));
    }
}