# Trading System - Order Book Implementation

## Overview
A comprehensive Java-based trading system implementing a full order book with real-time market data distribution. The system supports order management, quote management, trade execution, and real-time market data updates to subscribers.

## Features

### Core Functionality
- **Order Management**: Support for limit orders and quotes
- **Order Book**: Double-sided book (BUY/SELL) with price-time priority
- **Trade Execution**: Automated matching engine with partial and full fills
- **Market Data**: Real-time current market updates to subscribers
- **User Management**: User tracking with order history

### Key Components
- **ProductBook**: Manages order books for individual securities
- **CurrentMarketPublisher**: Implements observer pattern for market data distribution
- **PriceFactory**: Flyweight pattern for efficient price object management
- **UserManager**: Singleton for user management and order tracking

## System Architecture

### Packages
- `order` - Order and quote management, order book implementation
- `price` - Price representation and arithmetic operations
- `user` - User management and market data subscription
- `currentMarket` - Real-time market data distribution

### Design Patterns
- **Singleton** - Manager classes (UserManager, ProductManager, CurrentMarketPublisher)
- **Observer** - Market data distribution to subscribers
- **Flyweight** - Price object reuse via PriceFactory
- **Factory** - Price object creation
- **DTO** - Data transfer objects for order information

## Usage Example

// Initialize products and users

ProductManager.getInstance().addProduct("WMT");
UserManager.getInstance().init(new String[]{"ANA", "BOB", "COD"});

// Subscribe to market data

CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", user);

// Add quotes and orders

ProductManager.getInstance().addQuote(
    new Quote("WMT", PriceFactory.makePrice(15990), 75,
              PriceFactory.makePrice(16000), 75, "ANA"));

ProductManager.getInstance().addTradable(
    new Order("COD", "WMT", PriceFactory.makePrice(16000), 100, BUY));

## Key Classes

### Core Entities
- `Order` - Represents a limit order
- `Quote` - Represents a two-sided market maker quote
- `ProductBook` - Manages BUY/SELL sides for a security
- `ProductBookSide` - Individual side of the order book

### Management Classes
- `ProductManager` - Manages all product books
- `UserManager` - Manages users and their orders
- `CurrentMarketTracker` - Tracks and publishes market data changes

### Data Transfer
- `TradableDTO` - Record for transferring order state
- `CurrentMarketSide` - Represents current market price and volume

## Testing
The `Main.java` class provides comprehensive test scenarios demonstrating:
- Product and user initialization
- Quote and order management
- Trade execution
- Market data subscription and updates
- Order cancellation and book management

## Technical Notes
- Prices are represented in cents (integer) for precision
- Order IDs are generated using system nanoTime for uniqueness
- Market data updates are triggered on all book modifications
- The system maintains proper separation of concerns between management layers

This implementation provides a robust foundation for electronic trading systems with real-time market data distribution and efficient order matching.
