# 🏫 Smart Campus Energy Monitoring Platform | 智慧校园能耗监测与管理平台

> 2025秋《软件设计与体系结构》期末大作业

![Vue 3](https://img.shields.io/badge/Vue-3.5.25-42b983?style=flat-square&logo=vue.js)
![Element Plus](https://img.shields.io/badge/Element-Plus-409eff?style=flat-square&logo=element)
![Spring Boot](https://img.shields.io/badge/SpringBoot-2.6.13-6db33f?style=flat-square&logo=springboot)
![MyBatis](https://img.shields.io/badge/MyBatis-Plus-000000?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-5.7-4479a1?style=flat-square&logo=mysql)

## 📖 项目简介

本项目是一个基于 **Spring Boot + Vue 3** 前后端分离架构的全栈物联网管理系统。旨在解决校园场景下，对海量智能电表数据的实时采集、异常监控和可视化分析需求。

针对缺乏真实硬件的痛点，系统内置了一套**高扩展性的数据模拟引擎**，能够基于物理规律（P=UI）和时间策略生成高频时序数据，并自动检测过载与电压异常。

### ✨ 核心亮点

* **分层架构**：严格遵循 `Controller-Service-Repository` 分层设计，职责边界清晰。
* **设计模式落地**：
    * **策略模式 (Strategy)**：实现多场景数据模拟（日夜模式、故障注入）。
    * **观察者模式 (Observer)**：实现数据采集与告警检测的完全解耦。
* **真实物理模拟**：模拟器遵循物理公式，支持电压微波动、日夜功率差异及随机故障注入。
* **可视化大屏**：基于 ECharts 实现的深蓝科技风 Dashboard，展示实时功率曲线与 KPI 指标。

---

## 🛠️ 技术栈 (Tech Stack)

### 后端 (Backend)
* **核心框架**: Spring Boot 2.6.13
* **持久层**: MyBatis + MySQL 5.7 / 8.0
* **安全认证**: JWT (JSON Web Token)
* **接口文档**: Knife4j (Swagger 增强版)
* **工具库**: Lombok, FastJSON

### 前端 (Frontend)
* **核心框架**: Vue 3 (Composition API) + TypeScript
* **构建工具**: Vite
* **UI 组件库**: Element Plus
* **图表库**: Apache ECharts 5.x
* **网络请求**: Axios

---

## 🏗️ 系统架构与设计模式

### 1. 架构设计 (Architecture)
采用经典的 **MVC 三层架构** 配合 **贫血领域模型 (Anemic Domain Model)**：

* **Web 层 (`controller`)**: 负责 HTTP 请求接收、参数解析与统一结果封装。
* **Service 层 (`service`)**: 核心业务逻辑层。包含设备绑定校验（一房一表）、模拟器调度等。
* **DAO 层 (`mapper`)**: 负责 SQL 映射与复杂聚合查询（如 KPI 统计、联表排名）。

### 2. 设计模式应用 (Design Patterns)

#### 🕹️ 策略模式 (Strategy Pattern)
* **应用场景**: 数据模拟器 (`SimulatorService`)。
* **实现**: 定义 `SimulationStrategy` 接口，实现 `NormalStrategy` (正常日夜模式)、`OverloadStrategy` (过载异常)、`VoltageInstabilityStrategy` (电压不稳) 三种策略。
* **价值**: 符合开闭原则 (OCP)，新增故障类型无需修改核心调度代码。

#### 📡 观察者模式 (Observer Pattern)
* **应用场景**: 异常告警检测。
* **实现**: 模拟器生成数据后发布 `EnergyDataEvent` 事件，`AlarmEventListener` 监听该事件并判断电压/功率是否违规。
* **价值**: 实现单一职责原则 (SRP)，将“数据采集”与“业务监控”完全解耦。

---

## 📂 目录结构

```text
root
├── backend-core/             # 后端工程
│   ├── src/main/java/com/example/backendcore
│   │   ├── controller/       # Web接口
│   │   ├── service/          # 业务逻辑 (SimulatorService在此)
│   │   ├── mapper/           # DAO接口
│   │   ├── entity/           # 数据库实体
│   │   ├── strategy/         # [策略模式] 模拟算法实现
│   │   ├── event/            # [观察者模式] 事件定义
│   │   └── listener/         # [观察者模式] 告警监听器
│   └── src/main/resources/
│       └── mapper/           # MyBatis XML 文件
│
└── smart-campus-ui/          # 前端工程
    ├── src/
    │   ├── api/              # Axios 接口封装
    │   ├── views/            # Vue 页面 (Dashboard, Login...)
    │   ├── components/       # 公共组件
    │   └── utils/            # 工具类

```

## 🚀 快速开始 (Quick Start)
1. 环境准备

    - JDK 11+

    - Node.js 16+

    - MySQL 5.7+

    - IntelliJ IDEA & WebStorm (推荐)

2. 后端启动

    1. 在 MySQL 中创建数据库 energy_20231120136。

    2. 执行 src/main/resources/db/smart_campus.sql 脚本初始化表结构与数据。

    3. 修改 application.yml 中的数据库账号密码。

    4. 运行 BackendCoreApplication.java。

    5. 访问 http://localhost:8080/doc.html 查看接口文档。

3. 前端启动

```Bash

cd smart-campus-ui
npm install
npm run dev

```


访问 http://localhost:5173，默认账号: admin / 123456。

---

## 📸 功能截图
1. 综合监控大屏

展示实时总功率、在线设备数以及动态刷新的功率趋势图。
<img width="2188" height="1288" alt="Image" src="https://github.com/user-attachments/assets/e1dda24a-95b5-415c-a83a-64d16e1a3736" />

2. 实时能耗监测

左侧设备树选择，右侧展示该设备的实时电压、电流、功率曲线。
<img width="2190" height="1288" alt="Image" src="https://github.com/user-attachments/assets/f0466e4c-4a13-4d74-9ef5-f8e424a79e2e" />

3. 告警记录

自动捕获模拟器生成的“电压异常”与“功率过载”事件。
<img width="2171" height="1308" alt="Image" src="https://github.com/user-attachments/assets/20959a82-46f8-45fe-a7ae-315a54e77b20" />

---

## 📄 模拟器逻辑说明

系统后台启动了 @Scheduled 定时任务 (每5秒)：

1. 正常模式:

  - 08:00-22:00: 活跃模式，功率在额定值的 20%-90% 波动。

  - 22:00-08:00: 休眠模式，功率在 10W-100W 之间。

  - 电压始终在 215V-225V 间微小浮动。

2.故障注入:

  - 每生成 20-50 条数据后，随机触发一次故障。

  - 异常A (过载): 功率飙升至额定值的 1.2 倍。

  - 异常B (电压不稳): 电压突变为 180V 或 260V。

## 👤 作者

Created by shiki
